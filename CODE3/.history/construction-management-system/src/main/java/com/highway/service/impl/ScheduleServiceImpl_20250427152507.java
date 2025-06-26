package com.highway.service.impl;

import com.highway.model.Schedule;
import com.highway.model.WeatherInfo;
import com.highway.service.ScheduleService;
import com.highway.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final EntityManager entityManager;
    private final WeatherService weatherService;

    @Value("${app.schedule.max-continuous-work-hours}")
    private int maxContinuousWorkHours;

    @Value("${app.schedule.min-rest-hours}")
    private int minRestHours;

    @Override
    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        // 检查排班冲突
        if (checkScheduleConflict(schedule)) {
            throw new IllegalStateException("存在排班冲突，请检查排班时间");
        }

        // 检查工作时长
        if (!isValidWorkHours(schedule)) {
            throw new IllegalStateException("工作时长超过限制或休息时间不足");
        }

        // 获取天气信息
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(schedule.getLocation(), schedule.getDate());
        schedule.setWeatherInfo(weatherInfo);
        schedule.setWeatherImpact(weatherService.getWorkSuggestion(weatherInfo));

        entityManager.persist(schedule);
        return schedule;
    }

    @Override
    @Transactional
    public Schedule updateSchedule(Schedule schedule) {
        Schedule existingSchedule = entityManager.find(Schedule.class, schedule.getId());
        if (existingSchedule == null) {
            throw new IllegalArgumentException("排班记录不存在");
        }

        // 检查排班冲突（排除自身）
        if (checkScheduleConflict(schedule)) {
            throw new IllegalStateException("存在排班冲突，请检查排班时间");
        }

        // 更新天气信息
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(schedule.getLocation(), schedule.getDate());
        schedule.setWeatherInfo(weatherInfo);
        schedule.setWeatherImpact(weatherService.getWorkSuggestion(weatherInfo));

        return entityManager.merge(schedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = entityManager.find(Schedule.class, scheduleId);
        if (schedule != null) {
            entityManager.remove(schedule);
        }
    }

    @Override
    public List<Schedule> getWorkerSchedules(Long workerId, LocalDate startDate, LocalDate endDate) {
        TypedQuery<Schedule> query = entityManager.createQuery(
            "SELECT s FROM Schedule s WHERE s.workerId = :workerId " +
            "AND s.date BETWEEN :startDate AND :endDate " +
            "ORDER BY s.date, s.startTime",
            Schedule.class
        );
        query.setParameter("workerId", workerId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public List<Schedule> getProjectSchedules(Long projectId, LocalDate startDate, LocalDate endDate) {
        TypedQuery<Schedule> query = entityManager.createQuery(
            "SELECT s FROM Schedule s WHERE s.projectId = :projectId " +
            "AND s.date BETWEEN :startDate AND :endDate " +
            "ORDER BY s.date, s.startTime",
            Schedule.class
        );
        query.setParameter("projectId", projectId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public boolean checkScheduleConflict(Schedule schedule) {
        // 获取同一工人在同一天的所有排班
        List<Schedule> existingSchedules = getWorkerSchedules(
            schedule.getWorkerId(),
            schedule.getDate(),
            schedule.getDate()
        );

        // 排除自身（如果是更新操作）
        existingSchedules = existingSchedules.stream()
            .filter(s -> !s.getId().equals(schedule.getId()))
            .collect(Collectors.toList());

        // 检查时间冲突
        return existingSchedules.stream().anyMatch(schedule::checkConflict);
    }

    @Override
    public List<Schedule> getConflictingSchedules(Schedule schedule) {
        List<Schedule> existingSchedules = getWorkerSchedules(
            schedule.getWorkerId(),
            schedule.getDate(),
            schedule.getDate()
        );

        return existingSchedules.stream()
            .filter(s -> !s.getId().equals(schedule.getId()))
            .filter(schedule::checkConflict)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateWeatherBasedSuggestions(LocalDate date, String location) {
        // 获取指定日期和地点的所有排班
        TypedQuery<Schedule> query = entityManager.createQuery(
            "SELECT s FROM Schedule s WHERE s.date = :date AND s.location = :location",
            Schedule.class
        );
        query.setParameter("date", date);
        query.setParameter("location", location);

        List<Schedule> schedules = query.getResultList();
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(location, date);

        // 更新每个排班的天气信息和建议
        for (Schedule schedule : schedules) {
            schedule.setWeatherInfo(weatherInfo);
            schedule.setWeatherImpact(weatherService.getWorkSuggestion(weatherInfo));
            entityManager.merge(schedule);
        }
    }

    @Override
    public String getScheduleSuggestion(LocalDate date, String location) {
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(location, date);
        if (!weatherInfo.getSuitableForWork()) {
            return "不建议施工：" + weatherInfo.getWorkSuggestion();
        }
        return "可以施工：" + weatherInfo.getWorkSuggestion();
    }

    private boolean isValidWorkHours(Schedule schedule) {
        // 检查单次排班时长
        long workHours = java.time.Duration.between(schedule.getStartTime(), schedule.getEndTime()).toHours();
        if (workHours > maxContinuousWorkHours) {
            return false;
        }

        // 检查休息时间
        List<Schedule> previousDaySchedules = getWorkerSchedules(
            schedule.getWorkerId(),
            schedule.getDate().minusDays(1),
            schedule.getDate().minusDays(1)
        );

        if (!previousDaySchedules.isEmpty()) {
            Schedule lastSchedule = previousDaySchedules.get(previousDaySchedules.size() - 1);
            LocalTime previousEndTime = lastSchedule.getEndTime();
            LocalTime currentStartTime = schedule.getStartTime();

            // 计算休息时间（考虑跨天情况）
            long restHours = java.time.Duration.between(previousEndTime, currentStartTime).toHours();
            if (restHours < 0) {
                restHours += 24;
            }

            if (restHours < minRestHours) {
                return false;
            }
        }

        return true;
    }
} 