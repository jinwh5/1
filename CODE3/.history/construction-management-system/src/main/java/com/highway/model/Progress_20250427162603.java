package com.highway.model;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工程进度实体类
 */
@Data
public class Progress {
    private Long id;                     // 进度ID
    private Long projectId;              // 项目ID
    private String section;              // 路段名称
    private Integer plannedProgress;     // 计划进度(百分比)
    private Integer actualProgress;      // 实际进度(百分比)
    private LocalDate startDate;         // 开始日期
    private LocalDate plannedEndDate;    // 计划完成日期
    private LocalDate actualEndDate;     // 实际完成日期
    private String status;               // 状态(进行中/已完成/已延期)
    private String description;          // 进度描述
    private String obstacles;            // 遇到的障碍
    private String solutions;            // 解决方案
    private LocalDateTime updateTime;    // 更新时间
    private String updatedBy;            // 更新人
    private String remarks;              // 备注
} 