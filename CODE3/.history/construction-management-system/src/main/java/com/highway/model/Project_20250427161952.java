package com.highway.model;

import lombok.Data;
import java.time.LocalDate;

/**
 * 项目实体类
 */
@Data
public class Project {
    
    /**
     * 项目ID
     */
    private Long id;
    
    /**
     * 项目名称
     */
    private String name;
    
    /**
     * 项目描述
     */
    private String description;
    
    /**
     * 项目位置
     */
    private String location;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 项目状态（进行中、已完成、已暂停等）
     */
    private String status;
    
    /**
     * 项目经理
     */
    private String manager;
    
    /**
     * 项目预算
     */
    private Double budget;
    
    /**
     * 项目进度（百分比）
     */
    private Integer progress;
} 