package com.highway.controller;

import com.highway.model.Schedule;
import com.highway.service.interfaces.ScheduleService;
import com.highway.service.interfaces.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
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
        Schedule schedule = scheduleService.getSchedule(id);
        return ResponseEntity.ok(schedule);
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
        Schedule schedule = scheduleService.getSchedule(id);
        boolean hasConflict = scheduleService.checkScheduleConflict(schedule);
        String conflictMessage = hasConflict ? "发现时间冲突" : "未发现冲突";
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasConflict", hasConflict);
        response.put("conflictMessage", conflictMessage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/weather")
    public ResponseEntity<Map<String, Object>> updateWeather(@PathVariable Long id) {
        Schedule schedule = scheduleService.getSchedule(id);
        Schedule updatedSchedule = scheduleService.updateScheduleWeatherInfo(schedule);
        
        Map<String, Object> response = new HashMap<>();
        response.put("weatherCondition", updatedSchedule.getWeatherCondition());
        response.put("suitableForWork", updatedSchedule.getSuitableForWork());
        response.put("workSuggestion", updatedSchedule.getWeatherImpact());
        return ResponseEntity.ok(response);
    }
} 