package com.highway.controller;

import com.highway.model.Schedule;
import com.highway.service.interfaces.ScheduleService;
import com.highway.service.interfaces.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final WeatherService weatherService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.createSchedule(schedule));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String location) {
        
        List<Schedule> schedules;
        if (workerId != null) {
            schedules = scheduleService.getWorkerSchedules(workerId);
        } else if (projectId != null) {
            schedules = scheduleService.getProjectSchedules(projectId);
        } else if (date != null && location != null) {
            schedules = scheduleService.getSchedulesByDateAndLocation(date, location);
        } else {
            schedules = scheduleService.getAllSchedules();
        }
        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody Schedule schedule) {
        schedule.setId(id);
        return ResponseEntity.ok(scheduleService.updateSchedule(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/conflict")
    public ResponseEntity<Map<String, Object>> checkConflict(@PathVariable Long id) {
        boolean hasConflict = scheduleService.checkScheduleConflict(id);
        String conflictMessage = hasConflict ? "发现时间冲突" : "未发现冲突";
        return ResponseEntity.ok(Map.of(
            "hasConflict", hasConflict,
            "conflictMessage", conflictMessage
        ));
    }

    @PostMapping("/{id}/weather")
    public ResponseEntity<Map<String, Object>> updateWeather(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleById(id)
                .orElseThrow(() -> new RuntimeException("排班不存在"));
        
        var weatherInfo = weatherService.getWeatherInfo(schedule.getLocation(), schedule.getDate());
        boolean suitableForWork = weatherService.isSuitableForWork(weatherInfo);
        String workSuggestion = weatherService.getWorkSuggestion(weatherInfo);
        
        schedule.setWeatherCondition(weatherInfo.getWeatherCondition());
        schedule.setSuitableForWork(suitableForWork);
        schedule.setWorkSuggestion(workSuggestion);
        
        scheduleService.updateSchedule(schedule);
        
        return ResponseEntity.ok(Map.of(
            "weatherCondition", weatherInfo.getWeatherCondition(),
            "suitableForWork", suitableForWork,
            "workSuggestion", workSuggestion
        ));
    }
} 