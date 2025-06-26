package com.highway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 天气信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather_info")
public class WeatherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 关联的项目地点
    private String location;
    
    // 天气日期
    private LocalDate date;
    
    // 天气状况
    private String weatherCondition;  // 晴、多云、雨等
    private Double temperature;       // 温度
    private Double rainfall;          // 降雨量(mm)
    private Double windSpeed;         // 风速(m/s)
    
    // 天气预警信息
    private String weatherAlert;      // 天气预警信息
    
    // 是否适合施工
    private Boolean suitableForWork;  // 根据天气条件判断是否适合施工
    
    // 建议
    private String workSuggestion;    // 施工建议
} 