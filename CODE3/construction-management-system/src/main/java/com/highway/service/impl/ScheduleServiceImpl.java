package com.highway.service.impl;

import com.highway.model.Schedule;
import com.highway.model.WeatherInfo;
import com.highway.repository.interfaces.ScheduleRepository;
import com.highway.service.interfaces.ScheduleService;
import com.highway.service.interfaces.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 排班服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final WeatherService weatherService;

    @Override
    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        // 检查排班冲突
        boolean hasConflict = checkScheduleConflict(schedule);
        schedule.setHasConflict(hasConflict);
        
        // 更新天气信息
        schedule = updateScheduleWeatherInfo(schedule);
        
        // 保存排班
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public Schedule updateSchedule(Schedule schedule) {
        // 检查排班是否存在
        if (!scheduleRepository.findById(schedule.getId()).isPresent()) {
            throw new IllegalArgumentException("排班不存在");
        }
        
        // 检查排班冲突
        boolean hasConflict = checkScheduleConflict(schedule);
        schedule.setHasConflict(hasConflict);
        
        // 更新天气信息
        schedule = updateScheduleWeatherInfo(schedule);
        
        // 保存排班
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteSchedules(List<Long> ids) {
        scheduleRepository.deleteAllByIdIn(ids);
    }

    @Override
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("排班不存在"));
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Page<Schedule> getSchedules(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

    @Override
    public List<Schedule> getWorkerSchedules(Long workerId) {
        return scheduleRepository.findByWorkerId(workerId);
    }

    @Override
    public List<Schedule> getWorkerSchedules(Long workerId, LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByWorkerIdAndDateBetween(workerId, startDate, endDate);
    }

    @Override
    public List<Schedule> getProjectSchedules(Long projectId) {
        return scheduleRepository.findByProjectId(projectId);
    }

    @Override
    public List<Schedule> getProjectSchedules(Long projectId, LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByProjectIdAndDateBetween(projectId, startDate, endDate);
    }

    @Override
    public List<Schedule> getSchedulesByDateAndLocation(LocalDate date, String location) {
        return scheduleRepository.findByDateAndLocation(date, location);
    }

    @Override
    public boolean checkScheduleConflict(Schedule schedule) {
        // 获取同一天同一工人的所有排班
        List<Schedule> workerSchedules = scheduleRepository.findByWorkerIdAndDateBetween(
                schedule.getWorkerId(),
                schedule.getDate(),
                schedule.getDate()
        );
        
        // 检查时间冲突
        for (Schedule existingSchedule : workerSchedules) {
            // 跳过自身
            if (existingSchedule.getId() != null && existingSchedule.getId().equals(schedule.getId())) {
                continue;
            }
            
            // 检查时间是否重叠
            if (schedule.checkConflict(existingSchedule)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    @Transactional
    public Schedule updateScheduleWeatherInfo(Schedule schedule) {
        // 获取天气信息
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(schedule.getLocation(), schedule.getDate());
        
        // 更新排班的天气相关信息
        schedule.setWeatherCondition(weatherInfo.getWeatherCondition());
        schedule.setTemperature(weatherInfo.getTemperature());
        schedule.setRainfall(weatherInfo.getRainfall());
        schedule.setWindSpeed(weatherInfo.getWindSpeed());
        schedule.setWeatherAlert(weatherInfo.getWeatherAlert());
        schedule.setSuitableForWork(weatherInfo.getSuitableForWork());
        
        // 生成天气影响评估
        StringBuilder impact = new StringBuilder();
        if (!weatherInfo.getSuitableForWork()) {
            impact.append("当前天气不适合施工：");
            if (weatherInfo.getRainfall() > 5.0) {
                impact.append("降雨量过大；");
            }
            if (weatherInfo.getWindSpeed() > 10.0) {
                impact.append("风速过大；");
            }
            if (weatherInfo.getTemperature() < 5.0) {
                impact.append("温度过低；");
            }
            if (weatherInfo.getTemperature() > 35.0) {
                impact.append("温度过高；");
            }
        } else {
            impact.append("当前天气适合施工");
        }
        schedule.setWeatherImpact(impact.toString());
        
        return schedule;
    }
} 