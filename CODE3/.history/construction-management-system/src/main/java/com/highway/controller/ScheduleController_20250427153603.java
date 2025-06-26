package com.highway.controller;

import com.highway.model.Schedule;
import com.highway.service.interfaces.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.createSchedule(schedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        schedule.setId(id);
        return ResponseEntity.ok(scheduleService.updateSchedule(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getSchedule(id));
    }

    @GetMapping
    public ResponseEntity<Page<Schedule>> getSchedules(Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getSchedules(pageable));
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Schedule>> getWorkerSchedules(
            @PathVariable Long workerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(scheduleService.getWorkerSchedules(workerId, startDate, endDate));
        }
        return ResponseEntity.ok(scheduleService.getWorkerSchedules(workerId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Schedule>> getProjectSchedules(
            @PathVariable Long projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(scheduleService.getProjectSchedules(projectId, startDate, endDate));
        }
        return ResponseEntity.ok(scheduleService.getProjectSchedules(projectId));
    }

    @GetMapping("/location")
    public ResponseEntity<List<Schedule>> getSchedulesByDateAndLocation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String location) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDateAndLocation(date, location));
    }

    @PostMapping("/{id}/check-conflict")
    public ResponseEntity<Boolean> checkScheduleConflict(@PathVariable Long id) {
        Schedule schedule = scheduleService.getSchedule(id);
        return ResponseEntity.ok(scheduleService.checkScheduleConflict(schedule));
    }

    @PostMapping("/{id}/update-weather")
    public ResponseEntity<Schedule> updateScheduleWeatherInfo(@PathVariable Long id) {
        Schedule schedule = scheduleService.getSchedule(id);
        return ResponseEntity.ok(scheduleService.updateScheduleWeatherInfo(schedule));
    }
} 