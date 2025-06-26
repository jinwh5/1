package com.highway.service;

import com.highway.model.WeatherInfo;
import java.time.LocalDate;

public interface WeatherService {
    /**
     * 获取指定地点和日期的天气信息
     */
    WeatherInfo getWeatherInfo(String location, LocalDate date);
    
    /**
     * 更新天气信息
     */
    WeatherInfo updateWeatherInfo(String location);
    
    /**
     * 检查天气是否适合施工
     */
    boolean isSuitableForWork(WeatherInfo weatherInfo);
    
    /**
     * 获取施工建议
     */
    String getWorkSuggestion(WeatherInfo weatherInfo);
} 