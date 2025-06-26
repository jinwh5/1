package com.highway.service.interfaces;

import com.highway.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班服务接口
 */
public interface ScheduleService {
    /**
     * 创建排班
     * @param schedule 排班信息
     * @return 创建后的排班信息
     */
    Schedule createSchedule(Schedule schedule);
    
    /**
     * 更新排班
     * @param schedule 排班信息
     * @return 更新后的排班信息
     */
    Schedule updateSchedule(Schedule schedule);
    
    /**
     * 删除排班
     * @param id 排班ID
     */
    void deleteSchedule(Long id);
    
    /**
     * 批量删除排班
     * @param ids 排班ID列表
     */
    void deleteSchedules(List<Long> ids);
    
    /**
     * 获取排班信息
     * @param id 排班ID
     * @return 排班信息
     */
    Schedule getSchedule(Long id);
    
    /**
     * 获取所有排班
     * @return 排班列表
     */
    List<Schedule> getAllSchedules();
    
    /**
     * 分页获取排班
     * @param pageable 分页参数
     * @return 分页排班列表
     */
    Page<Schedule> getSchedules(Pageable pageable);
    
    /**
     * 获取工人的排班列表
     * @param workerId 工人ID
     * @return 排班列表
     */
    List<Schedule> getWorkerSchedules(Long workerId);
    
    /**
     * 获取工人在指定日期范围内的排班
     * @param workerId 工人ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<Schedule> getWorkerSchedules(Long workerId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取项目的排班列表
     * @param projectId 项目ID
     * @return 排班列表
     */
    List<Schedule> getProjectSchedules(Long projectId);
    
    /**
     * 获取项目在指定日期范围内的排班
     * @param projectId 项目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<Schedule> getProjectSchedules(Long projectId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取指定日期和地点的排班列表
     * @param date 日期
     * @param location 地点
     * @return 排班列表
     */
    List<Schedule> getSchedulesByDateAndLocation(LocalDate date, String location);
    
    /**
     * 检查排班冲突
     * @param schedule 待检查的排班
     * @return 是否存在冲突
     */
    boolean checkScheduleConflict(Schedule schedule);
    
    /**
     * 更新排班的天气相关信息
     * @param schedule 排班信息
     * @return 更新后的排班信息
     */
    Schedule updateScheduleWeatherInfo(Schedule schedule);
} 