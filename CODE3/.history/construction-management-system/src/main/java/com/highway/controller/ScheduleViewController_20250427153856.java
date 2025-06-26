package com.highway.controller;

import com.highway.service.interfaces.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ScheduleViewController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public String schedulePage(
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String location,
            Model model) {
        
        // 添加数据到模型
        if (workerId != null) {
            model.addAttribute("schedules", scheduleService.getWorkerSchedules(workerId));
        } else if (projectId != null) {
            model.addAttribute("schedules", scheduleService.getProjectSchedules(projectId));
        } else if (date != null && location != null) {
            model.addAttribute("schedules", scheduleService.getSchedulesByDateAndLocation(date, location));
        } else {
            model.addAttribute("schedules", scheduleService.getAllSchedules());
        }
        
        return "schedule/index";
    }
} 