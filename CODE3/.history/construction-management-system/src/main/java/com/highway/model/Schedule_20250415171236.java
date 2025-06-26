package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 排班实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    // 排班ID
    private Long id;
    
    // 工人ID
    private Long workerId;
    
    // 项目ID
    private Long projectId;
    
    // 排班日期
    private LocalDate date;
    
    // 班次类型（早班、中班、晚班等）
    private String shiftType;
    
    // 时间信息
    private LocalTime startTime;  // 开始时间
    private LocalTime endTime;    // 结束时间
    
    // 工作地点
    private String location;
    
    // 状态（待确认、已确认、已完成、已取消等）
    private String status;
    
    // 备注
    private String remarks;
} 