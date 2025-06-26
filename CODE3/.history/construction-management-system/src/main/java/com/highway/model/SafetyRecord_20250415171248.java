package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 安全记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyRecord {
    // 安全记录ID
    private Long id;
    
    // 项目ID
    private Long projectId;
    
    // 事件日期
    private LocalDate eventDate;
    
    // 事件类型（事故、安全检查、培训等）
    private String eventType;
    
    // 事件描述
    private String description;
    
    // 处理人
    private String handler;
    
    // 状态（已报告、处理中、已解决等）
    private String status;
    
    // 备注
    private String remarks;
} 