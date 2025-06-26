package com.highway.service.interfaces;

import com.highway.model.WeatherInfo;
import java.time.LocalDate;

/**
 * 天气服务接口
 */
public interface WeatherService {
    /**
     * 获取指定地点和日期的天气信息
     * @param location 地点
     * @param date 日期
     * @return 天气信息
     */
    WeatherInfo getWeatherInfo(String location, LocalDate date);
    
    /**
     * 更新天气信息
     * @param location 地点
     * @return 更新后的天气信息
     */
    WeatherInfo updateWeatherInfo(String location);
    
    /**
     * 检查天气是否适合施工
     * @param weatherInfo 天气信息
     * @return 是否适合施工
     */
    boolean isSuitableForWork(WeatherInfo weatherInfo);
    
    /**
     * 获取施工建议
     * @param weatherInfo 天气信息
     * @return 施工建议
     */
    String getWorkSuggestion(WeatherInfo weatherInfo);
} 