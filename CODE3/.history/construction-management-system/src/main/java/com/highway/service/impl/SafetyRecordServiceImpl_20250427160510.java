package com.highway.service.impl;

import com.highway.model.SafetyRecord;
import com.highway.repository.interfaces.SafetyRecordRepository;
import com.highway.service.interfaces.SafetyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 安全监控记录服务实现类
 */
@Service
public class SafetyRecordServiceImpl implements SafetyRecordService {

    @Autowired
    private SafetyRecordRepository safetyRecordRepository;

    @Override
    public SafetyRecord save(SafetyRecord record) {
        return safetyRecordRepository.save(record);
    }

    @Override
    public Optional<SafetyRecord> findById(Long id) {
        return safetyRecordRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return safetyRecordRepository.findById(id).isPresent();
    }

    @Override
    public List<SafetyRecord> findAll() {
        return safetyRecordRepository.findAll();
    }

    @Override
    public Page<SafetyRecord> findAll(Pageable pageable) {
        return safetyRecordRepository.findAll(pageable);
    }

    @Override
    public List<SafetyRecord> findByWorkerId(Long workerId) {
        return safetyRecordRepository.findByWorkerId(workerId);
    }

    @Override
    public List<SafetyRecord> findByProjectId(Long projectId) {
        return safetyRecordRepository.findByProjectId(projectId);
    }

    @Override
    public List<SafetyRecord> findByEventType(String eventType) {
        return safetyRecordRepository.findByEventType(eventType);
    }

    @Override
    public List<SafetyRecord> findBySeverityLevel(String severityLevel) {
        return safetyRecordRepository.findBySeverityLevel(severityLevel);
    }

    @Override
    public List<SafetyRecord> findByStatus(String status) {
        return safetyRecordRepository.findByStatus(status);
    }

    @Override
    public List<SafetyRecord> findByOccurrenceTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return safetyRecordRepository.findByOccurrenceTimeBetween(startTime, endTime);
    }

    @Override
    public void deleteById(Long id) {
        safetyRecordRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        safetyRecordRepository.deleteAllByIdIn(ids);
    }

    @Override
    public Page<SafetyRecord> findRecords(
            Long workerId,
            Long projectId,
            String eventType,
            String severityLevel,
            String status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String location,
            Pageable pageable) {
        
        // 获取所有记录
        List<SafetyRecord> allRecords = safetyRecordRepository.findAll();
        
        // 应用过滤条件
        List<SafetyRecord> filteredRecords = allRecords.stream()
            .filter(record -> workerId == null || record.getWorkerId().equals(workerId))
            .filter(record -> projectId == null || record.getProjectId().equals(projectId))
            .filter(record -> eventType == null || record.getEventType().equals(eventType))
            .filter(record -> severityLevel == null || record.getSeverityLevel().equals(severityLevel))
            .filter(record -> status == null || record.getStatus().equals(status))
            .filter(record -> startTime == null || !record.getOccurrenceTime().isBefore(startTime))
            .filter(record -> endTime == null || !record.getOccurrenceTime().isAfter(endTime))
            .filter(record -> location == null || record.getLocation().contains(location))
            .collect(Collectors.toList());
        
        // 计算分页
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredRecords.size());
        
        // 返回分页结果
        return new PageImpl<>(
            filteredRecords.subList(start, end),
            pageable,
            filteredRecords.size()
        );
    }
} 