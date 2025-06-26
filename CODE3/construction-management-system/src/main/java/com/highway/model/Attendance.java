package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考勤记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    // 考勤记录ID
    private Long id;
    
    // 关联工人ID
    private Long workerId;
    
    // 考勤日期
    private LocalDate date;
    
    // 签到时间和签退时间
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    
    // 状态（正常、迟到、早退、缺勤等）
    private String status;
    
    // 工时信息
    private Double workHours;    // 工作时长
    private Double overtimeHours; // 加班时长
    
    // 备注信息
    private String remarks;
} 