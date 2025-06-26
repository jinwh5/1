package com.highway.controller;

import com.highway.model.SafetyRecord;
import com.highway.service.interfaces.SafetyRecordService;
import com.highway.service.interfaces.WorkerService;
import com.highway.service.interfaces.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 安全监控管理控制器
 */
@Controller
@RequestMapping("/safety")
public class SafetyController {

    @Autowired
    private SafetyRecordService safetyRecordService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private ProjectService projectService;

    /**
     * 安全监控管理主页
     */
    @GetMapping
    public String index(Model model) {
        // 获取安全记录统计数据
        model.addAttribute("safetyDays", safetyRecordService.getSafetyDays());
        model.addAttribute("minorIncidents", safetyRecordService.getIncidentCountBySeverity("轻微"));
        model.addAttribute("moderateIncidents", safetyRecordService.getIncidentCountBySeverity("一般"));
        model.addAttribute("majorIncidents", safetyRecordService.getIncidentCountBySeverity("重大"));

        // 获取安全记录列表
        List<SafetyRecord> records = safetyRecordService.findAll();
        model.addAttribute("records", records);

        // 获取工人和项目列表（用于添加记录时的下拉选择）
        model.addAttribute("workers", workerService.findAll());
        model.addAttribute("projects", projectService.findAll());

        return "safety/index";
    }

    /**
     * 获取安全记录列表（分页）
     */
    @GetMapping("/api/records")
    @ResponseBody
    public Page<SafetyRecord> getRecords(
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String severityLevel,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 构建查询条件
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurrenceTime"));
        
        // 转换日期为日期时间
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
        
        // 调用服务层方法
        return safetyRecordService.findRecords(workerId, projectId, eventType, severityLevel, 
                status, startDateTime, endDateTime, location, pageRequest);
    }

    /**
     * 获取单个安全记录
     */
    @GetMapping("/{id}")
    @ResponseBody
    public SafetyRecord getById(@PathVariable Long id) {
        return safetyRecordService.findById(id)
                .orElseThrow(() -> new RuntimeException("安全记录不存在"));
    }

    /**
     * 创建安全记录
     */
    @PostMapping
    @ResponseBody
    public SafetyRecord create(@RequestBody SafetyRecord record) {
        return safetyRecordService.save(record);
    }

    /**
     * 更新安全记录
     */
    @PutMapping("/{id}")
    @ResponseBody
    public SafetyRecord update(@PathVariable Long id, @RequestBody SafetyRecord record) {
        record.setId(id);
        return safetyRecordService.save(record);
    }

    /**
     * 删除安全记录
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        safetyRecordService.deleteById(id);
    }

    /**
     * 根据工人ID获取安全记录
     */
    @GetMapping("/api/records/worker/{workerId}")
    @ResponseBody
    public List<SafetyRecord> getRecordsByWorker(@PathVariable Long workerId) {
        return safetyRecordService.findByWorkerId(workerId);
    }

    /**
     * 根据项目ID获取安全记录
     */
    @GetMapping("/api/records/project/{projectId}")
    @ResponseBody
    public List<SafetyRecord> getRecordsByProject(@PathVariable Long projectId) {
        return safetyRecordService.findByProjectId(projectId);
    }

    /**
     * 根据事件类型获取安全记录
     */
    @GetMapping("/api/records/event-type/{eventType}")
    @ResponseBody
    public List<SafetyRecord> getRecordsByEventType(@PathVariable String eventType) {
        return safetyRecordService.findByEventType(eventType);
    }

    /**
     * 根据严重程度获取安全记录
     */
    @GetMapping("/api/records/severity/{severityLevel}")
    @ResponseBody
    public List<SafetyRecord> getRecordsBySeverityLevel(@PathVariable String severityLevel) {
        return safetyRecordService.findBySeverityLevel(severityLevel);
    }

    /**
     * 根据状态获取安全记录
     */
    @GetMapping("/api/records/status/{status}")
    @ResponseBody
    public List<SafetyRecord> getRecordsByStatus(@PathVariable String status) {
        return safetyRecordService.findByStatus(status);
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String severityLevel,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            Model model) {
        
        List<SafetyRecord> records = safetyRecordService.search(
                workerId, projectId, eventType, severityLevel, status, startTime, endTime);
        model.addAttribute("records", records);
        
        return "safety/index";
    }
} 