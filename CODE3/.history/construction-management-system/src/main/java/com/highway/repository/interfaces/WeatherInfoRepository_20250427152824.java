package com.highway.repository.interfaces;

import com.highway.model.WeatherInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 天气信息仓库接口
 */
public interface WeatherInfoRepository {
    /**
     * 保存天气信息
     * @param weatherInfo 天气信息
     * @return 保存后的天气信息
     */
    WeatherInfo save(WeatherInfo weatherInfo);
    
    /**
     * 根据ID查找天气信息
     * @param id 天气信息ID
     * @return 天气信息（可能为空）
     */
    Optional<WeatherInfo> findById(Long id);
    
    /**
     * 查询所有天气信息
     * @return 天气信息列表
     */
    List<WeatherInfo> findAll();
    
    /**
     * 分页查询所有天气信息
     * @param pageable 分页信息
     * @return 分页天气信息列表
     */
    Page<WeatherInfo> findAll(Pageable pageable);
    
    /**
     * 根据地点和日期查询天气信息
     * @param location 地点
     * @param date 日期
     * @return 天气信息（可能为空）
     */
    Optional<WeatherInfo> findByLocationAndDate(String location, LocalDate date);
    
    /**
     * 根据地点和日期范围查询天气信息列表
     * @param location 地点
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 天气信息列表
     */
    List<WeatherInfo> findByLocationAndDateBetween(String location, LocalDate startDate, LocalDate endDate);
    
    /**
     * 删除天气信息
     * @param id 天气信息ID
     */
    void deleteById(Long id);
    
    /**
     * 批量删除天气信息
     * @param ids ID列表
     */
    void deleteAllByIdIn(List<Long> ids);
} 