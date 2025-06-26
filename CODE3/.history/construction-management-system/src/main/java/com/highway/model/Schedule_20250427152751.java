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
    
    // 天气相关
    private String weatherCondition;  // 天气状况
    private Double temperature;       // 温度
    private Double rainfall;          // 降雨量
    private Double windSpeed;         // 风速
    private String weatherAlert;      // 天气预警
    private Boolean suitableForWork;  // 是否适合施工
    private String weatherImpact;     // 天气影响评估
    
    // 是否存在排班冲突
    private Boolean hasConflict;
    
    // 冲突描述
    private String conflictDescription;
    
    // 备注
    private String remarks;
    
    // 检查排班冲突
    public boolean checkConflict(Schedule other) {
        if (!this.date.equals(other.date) || !this.workerId.equals(other.workerId)) {
            return false;
        }
        
        // 检查时间重叠
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }
} 