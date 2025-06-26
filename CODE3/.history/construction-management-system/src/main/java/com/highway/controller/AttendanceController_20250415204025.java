package com.highway.controller;

import com.highway.model.Attendance;
import com.highway.model.Worker;
import com.highway.service.interfaces.AttendanceService;
import com.highway.service.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考勤管理控制器
 */
@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    // 考勤服务
    private final AttendanceService attendanceService;
    // 工人服务
    private final WorkerService workerService;
    
    /**
     * 考勤列表页面
     */
    @GetMapping
    public String listAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Attendance> attendancePage;
        
        // 根据条件查询
        if (workerId != null) {
            attendancePage = attendanceService.getAttendanceByWorker(workerId, pageable);
            model.addAttribute("workerId", workerId);
            model.addAttribute("workerName", workerService.getWorkerById(workerId).getName());
        } else if (startDate != null && endDate != null) {
            attendancePage = attendanceService.getAttendanceByDateRange(startDate, endDate, pageable);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } else if (status != null && !status.isEmpty()) {
            attendancePage = attendanceService.getAttendanceByStatus(status, pageable);
            model.addAttribute("status", status);
        } else {
            attendancePage = attendanceService.getAllAttendance(pageable);
        }
        
        // 获取所有工人列表，用于选择框和显示工人姓名
        List<Worker> workers = workerService.getAllWorkers();
        
        // 创建工人ID到姓名的映射，方便在页面上显示
        java.util.Map<Long, String> workerNamesMap = new java.util.HashMap<>();
        for (Worker worker : workers) {
            workerNamesMap.put(worker.getId(), worker.getName());
        }
        model.addAttribute("workerNames", workerNamesMap);
        
        model.addAttribute("attendanceRecords", attendancePage.getContent());
        model.addAttribute("workers", workers);
        model.addAttribute("currentPage", attendancePage.getNumber());
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalItems", attendancePage.getTotalElements());
        
        return "attendance/list";
    }
    
    /**
     * 添加考勤记录表单页面
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("workers", workerService.getAllWorkers());
        return "attendance/form";
    }
    
    /**
     * 编辑考勤记录表单页面
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        model.addAttribute("attendance", attendance);
        model.addAttribute("workers", workerService.getAllWorkers());
        return "attendance/form";
    }
    
    /**
     * 保存考勤记录
     */
    @PostMapping("/save")
    public String saveAttendance(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "workerId", required = true) Long workerId,
            @RequestParam(value = "status", required = true) String status,
            @RequestParam(value = "date", required = true) String dateStr,
            @RequestParam(value = "checkInTime", required = false) String checkInTimeStr,
            @RequestParam(value = "checkOutTime", required = false) String checkOutTimeStr,
            @RequestParam(value = "workHours", required = false) Double workHours,
            @RequestParam(value = "overtimeHours", required = false) Double overtimeHours,
            @RequestParam(value = "remarks", required = false) String remarks,
            RedirectAttributes redirectAttributes) {
        
        try {
            // 创建新的Attendance对象
            Attendance attendance = new Attendance();
            
            // 设置基本字段
            attendance.setId(id);
            attendance.setWorkerId(workerId);
            attendance.setStatus(status);
            attendance.setWorkHours(workHours != null ? workHours : 0.0);
            attendance.setOvertimeHours(overtimeHours != null ? overtimeHours : 0.0);
            attendance.setRemarks(remarks);
            
            // 解析日期
            try {
                LocalDate date = LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ISO_DATE);
                attendance.setDate(date);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "日期格式无效: " + dateStr);
                return id == null ? "redirect:/attendance/add" : "redirect:/attendance/edit/" + id;
            }
            
            // 解析签到时间
            if (checkInTimeStr != null && !checkInTimeStr.isEmpty()) {
                try {
                    // 确保时间字符串格式正确
                    if (!checkInTimeStr.contains(":")) {
                        checkInTimeStr += ":00";
                    } else if (checkInTimeStr.split(":").length < 3) {
                        checkInTimeStr += ":00";
                    }
                    
                    LocalDateTime checkInTime = LocalDateTime.parse(
                            checkInTimeStr.replace(" ", "T") + (checkInTimeStr.contains("Z") ? "" : ""), 
                            java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    attendance.setCheckInTime(checkInTime);
                } catch (Exception e) {
                    e.printStackTrace(); // 调试用
                    redirectAttributes.addFlashAttribute("error", "签到时间格式无效: " + checkInTimeStr + ", 错误: " + e.getMessage());
                    return id == null ? "redirect:/attendance/add" : "redirect:/attendance/edit/" + id;
                }
            }
            
            // 解析签退时间
            if (checkOutTimeStr != null && !checkOutTimeStr.isEmpty()) {
                try {
                    // 确保时间字符串格式正确
                    if (!checkOutTimeStr.contains(":")) {
                        checkOutTimeStr += ":00";
                    } else if (checkOutTimeStr.split(":").length < 3) {
                        checkOutTimeStr += ":00";
                    }
                    
                    LocalDateTime checkOutTime = LocalDateTime.parse(
                            checkOutTimeStr.replace(" ", "T") + (checkOutTimeStr.contains("Z") ? "" : ""), 
                            java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    attendance.setCheckOutTime(checkOutTime);
                } catch (Exception e) {
                    e.printStackTrace(); // 调试用
                    redirectAttributes.addFlashAttribute("error", "签退时间格式无效: " + checkOutTimeStr + ", 错误: " + e.getMessage());
                    return id == null ? "redirect:/attendance/add" : "redirect:/attendance/edit/" + id;
                }
            }
            
            // 如果是新增考勤并且没有设置工时，自动计算工时
            if (id == null && 
                workHours == null && 
                attendance.getCheckInTime() != null && 
                attendance.getCheckOutTime() != null) {
                // 计算工作时长（小时）
                double hours = calculateHours(attendance.getCheckInTime(), attendance.getCheckOutTime());
                attendance.setWorkHours(hours);
            }
            
            // 保存考勤记录
            attendanceService.saveAttendance(attendance);
            redirectAttributes.addFlashAttribute("message", "考勤记录保存成功");
            return "redirect:/attendance";
            
        } catch (Exception e) {
            e.printStackTrace(); // 调试用
            redirectAttributes.addFlashAttribute("error", "保存考勤记录失败: " + e.getMessage());
            return id == null ? "redirect:/attendance/add" : "redirect:/attendance/edit/" + id;
        }
    }
    
    /**
     * 查看考勤记录详情
     */
    @GetMapping("/view/{id}")
    public String viewAttendance(@PathVariable Long id, Model model) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        Worker worker = workerService.getWorkerById(attendance.getWorkerId());
        
        model.addAttribute("attendance", attendance);
        model.addAttribute("worker", worker);
        return "attendance/view";
    }
    
    /**
     * 删除考勤记录
     */
    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        attendanceService.deleteAttendance(id);
        redirectAttributes.addFlashAttribute("message", "考勤记录删除成功");
        return "redirect:/attendance";
    }
    
    /**
     * 获取工人的考勤记录
     */
    @GetMapping("/worker/{workerId}")
    public String getWorkerAttendance(
            @PathVariable Long workerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Worker worker = workerService.getWorkerById(workerId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Attendance> attendancePage = attendanceService.getAttendanceByWorker(workerId, pageable);
        
        model.addAttribute("worker", worker);
        model.addAttribute("attendanceRecords", attendancePage.getContent());
        model.addAttribute("currentPage", attendancePage.getNumber());
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalItems", attendancePage.getTotalElements());
        
        return "attendance/worker-attendance";
    }
    
    /**
     * 考勤统计页面
     */
    @GetMapping("/statistics")
    public String attendanceStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model
    ) {
        // 如果没有指定日期范围，默认为当前月
        if (startDate == null) {
            LocalDate now = LocalDate.now();
            startDate = now.withDayOfMonth(1);
            endDate = now.withDayOfMonth(now.lengthOfMonth());
        }
        
        // 获取统计数据
        long totalRecords = attendanceService.countAttendanceByDateRange(startDate, endDate);
        long normalRecords = attendanceService.countAttendanceByStatusAndDateRange("正常", startDate, endDate);
        long lateRecords = attendanceService.countAttendanceByStatusAndDateRange("迟到", startDate, endDate);
        long earlyLeaveRecords = attendanceService.countAttendanceByStatusAndDateRange("早退", startDate, endDate);
        long absentRecords = attendanceService.countAttendanceByStatusAndDateRange("缺勤", startDate, endDate);
        
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("normalRecords", normalRecords);
        model.addAttribute("lateRecords", lateRecords);
        model.addAttribute("earlyLeaveRecords", earlyLeaveRecords);
        model.addAttribute("absentRecords", absentRecords);
        
        // 获取工人考勤统计数据
        List<Object[]> workerStats = attendanceService.getWorkerAttendanceStatsByDateRange(startDate, endDate);
        model.addAttribute("workerStats", workerStats);
        
        return "attendance/statistics";
    }
    
    /**
     * 计算两个时间之间的小时数
     */
    private double calculateHours(LocalDateTime start, LocalDateTime end) {
        // 计算时间差（分钟）
        long minutes = java.time.Duration.between(start, end).toMinutes();
        // 转换为小时（保留一位小数）
        return Math.round(minutes / 6.0) / 10.0;
    }
} 