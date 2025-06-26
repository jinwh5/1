package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 工人实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worker {
    // 工人ID
    private Long id;
    
    // 基本信息
    private String name;        // 姓名
    private String gender;      // 性别
    private Integer age;        // 年龄
    private String idCard;      // 身份证号
    private String phone;       // 电话号码
    private String position;    // 职位
    
    // 就职信息
    private LocalDate hireDate; // 入职日期
    private String status;      // 状态（在职/离职等）
    
    // 其他信息
    private String address;     // 地址
    private String remarks;     // 备注
} 