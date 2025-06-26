package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 安全监控记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyRecord {
    // 记录ID
    private Long id;
    
    // 关联工人ID
    private Long workerId;
    
    // 关联项目ID
    private Long projectId;
    
    // 事件类型（违规操作、安全隐患、事故等）
    private String eventType;
    
    // 事件等级（一般、严重、特别严重）
    private String severityLevel;
    
    // 事件发生时间
    private LocalDateTime occurrenceTime;
    
    // 事件地点
    private String location;
    
    // 事件描述
    private String description;
    
    // 处理状态（待处理、处理中、已处理、已关闭）
    private String status;
    
    // 处理措施
    private String measures;
    
    // 处理人
    private String handler;
    
    // 处理时间
    private LocalDateTime handleTime;
    
    // 处理结果
    private String result;
    
    // 备注
    private String remarks;
} 