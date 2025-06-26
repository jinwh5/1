package com.highway.repository.interfaces;

import com.highway.model.SafetyRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 安全监控记录仓库接口
 */
public interface SafetyRecordRepository {
    /**
     * 保存安全监控记录
     */
    SafetyRecord save(SafetyRecord record);
    
    /**
     * 根据ID查找安全监控记录
     */
    Optional<SafetyRecord> findById(Long id);
    
    /**
     * 查找所有安全监控记录
     */
    List<SafetyRecord> findAll();
    
    /**
     * 分页查询安全监控记录
     */
    Page<SafetyRecord> findAll(Pageable pageable);
    
    /**
     * 根据工人ID查询安全监控记录
     */
    List<SafetyRecord> findByWorkerId(Long workerId);
    
    /**
     * 根据项目ID查询安全监控记录
     */
    List<SafetyRecord> findByProjectId(Long projectId);
    
    /**
     * 根据事件类型查询安全监控记录
     */
    List<SafetyRecord> findByEventType(String eventType);
    
    /**
     * 根据严重程度查询安全监控记录
     */
    List<SafetyRecord> findBySeverityLevel(String severityLevel);
    
    /**
     * 根据状态查询安全监控记录
     */
    List<SafetyRecord> findByStatus(String status);
    
    /**
     * 根据时间范围查询安全监控记录
     */
    List<SafetyRecord> findByOccurrenceTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据ID删除安全监控记录
     */
    void deleteById(Long id);
    
    /**
     * 批量删除安全监控记录
     */
    void deleteAllByIdIn(List<Long> ids);
} 