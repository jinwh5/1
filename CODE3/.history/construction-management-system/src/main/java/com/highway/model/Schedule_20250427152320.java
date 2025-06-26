package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 排班实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 工人ID
    @Column(nullable = false)
    private Long workerId;
    
    // 项目ID
    @Column(nullable = false)
    private Long projectId;
    
    // 排班日期
    @Column(nullable = false)
    private LocalDate date;
    
    // 班次类型（早班、中班、晚班等）
    private String shiftType;
    
    // 时间信息
    @Column(nullable = false)
    private LocalTime startTime;  // 开始时间
    @Column(nullable = false)
    private LocalTime endTime;    // 结束时间
    
    // 工作地点
    @Column(nullable = false)
    private String location;
    
    // 状态（待确认、已确认、已完成、已取消等）
    private String status;
    
    // 天气相关
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_info_id")
    private WeatherInfo weatherInfo;
    
    // 天气影响评估
    private String weatherImpact;  // 天气对工作的影响评估
    
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