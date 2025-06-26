package com.highway.repository.interfaces;

import com.highway.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 考勤信息仓库接口
 */
public interface AttendanceRepository {
    /**
     * 保存考勤记录
     * @param attendance 考勤记录
     * @return 保存后的考勤记录
     */
    Attendance save(Attendance attendance);
    
    /**
     * 根据ID查找考勤记录
     * @param id 考勤记录ID
     * @return 考勤记录（可能为空）
     */
    Optional<Attendance> findById(Long id);
    
    /**
     * 查询所有考勤记录
     * @return 考勤记录列表
     */
    List<Attendance> findAll();
    
    /**
     * 分页查询所有考勤记录
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> findAll(Pageable pageable);
    
    /**
     * 根据工人ID查询考勤记录
     * @param workerId 工人ID
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> findByWorkerId(Long workerId, Pageable pageable);
    
    /**
     * 根据日期范围查询考勤记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * 根据日期范围查询所有考勤记录（不分页）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据状态查询考勤记录
     * @param status 考勤状态
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> findByStatus(String status, Pageable pageable);
    
    /**
     * 根据工人ID和日期查询考勤记录
     * @param workerId 工人ID
     * @param date 日期
     * @return 考勤记录（可能为空）
     */
    Optional<Attendance> findByWorkerIdAndDate(Long workerId, LocalDate date);
    
    /**
     * 删除考勤记录
     * @param id 考勤记录ID
     */
    void deleteById(Long id);
    
    /**
     * 计算日期范围内的考勤记录数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 记录数量
     */
    long countByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 计算日期范围内特定状态的考勤记录数量
     * @param status 考勤状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 记录数量
     */
    long countByStatusAndDateBetween(String status, LocalDate startDate, LocalDate endDate);
} 