package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 项目实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    // 项目ID
    private Long id;
    
    // 基本信息
    private String name;      // 项目名称
    private String location;  // 项目地点
    
    // 时间信息
    private LocalDate startDate;  // 开始日期
    private LocalDate endDate;    // 结束日期
    
    // 状态（未开始、进行中、已完成、已暂停等）
    private String status;
    
    // 描述信息
    private String description;
} 