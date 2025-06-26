package com.highway.service;

import com.highway.model.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    /**
     * 创建新排班
     */
    Schedule createSchedule(Schedule schedule);
    
    /**
     * 更新排班信息
     */
    Schedule updateSchedule(Schedule schedule);
    
    /**
     * 删除排班
     */
    void deleteSchedule(Long scheduleId);
    
    /**
     * 获取指定工人的排班列表
     */
    List<Schedule> getWorkerSchedules(Long workerId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取指定项目的排班列表
     */
    List<Schedule> getProjectSchedules(Long projectId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 检查排班冲突
     */
    boolean checkScheduleConflict(Schedule schedule);
    
    /**
     * 获取冲突的排班列表
     */
    List<Schedule> getConflictingSchedules(Schedule schedule);
    
    /**
     * 根据天气情况更新排班建议
     */
    void updateWeatherBasedSuggestions(LocalDate date, String location);
    
    /**
     * 获取指定日期和地点的排班建议
     */
    String getScheduleSuggestion(LocalDate date, String location);
} 