package com.highway.repository.interfaces;

import com.highway.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 排班信息仓库接口
 */
public interface ScheduleRepository {
    /**
     * 保存排班信息
     * @param schedule 排班信息
     * @return 保存后的排班信息
     */
    Schedule save(Schedule schedule);
    
    /**
     * 根据ID查找排班
     * @param id 排班ID
     * @return 排班信息（可能为空）
     */
    Optional<Schedule> findById(Long id);
    
    /**
     * 查询所有排班
     * @return 排班列表
     */
    List<Schedule> findAll();
    
    /**
     * 分页查询所有排班
     * @param pageable 分页信息
     * @return 分页排班列表
     */
    Page<Schedule> findAll(Pageable pageable);
    
    /**
     * 根据工人ID查询排班列表
     * @param workerId 工人ID
     * @return 排班列表
     */
    List<Schedule> findByWorkerId(Long workerId);
    
    /**
     * 根据工人ID和日期范围查询排班列表
     * @param workerId 工人ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<Schedule> findByWorkerIdAndDateBetween(Long workerId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据项目ID查询排班列表
     * @param projectId 项目ID
     * @return 排班列表
     */
    List<Schedule> findByProjectId(Long projectId);
    
    /**
     * 根据项目ID和日期范围查询排班列表
     * @param projectId 项目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<Schedule> findByProjectIdAndDateBetween(Long projectId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据日期和地点查询排班列表
     * @param date 日期
     * @param location 地点
     * @return 排班列表
     */
    List<Schedule> findByDateAndLocation(LocalDate date, String location);
    
    /**
     * 删除排班信息
     * @param id 排班ID
     */
    void deleteById(Long id);
    
    /**
     * 批量删除排班信息
     * @param ids ID列表
     */
    void deleteAllByIdIn(List<Long> ids);
} 