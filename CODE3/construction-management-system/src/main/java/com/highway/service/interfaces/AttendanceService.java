package com.highway.service.interfaces;

import com.highway.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * 考勤服务接口
 */
public interface AttendanceService {
    /**
     * 保存考勤记录
     * @param attendance 考勤记录
     * @return 保存后的考勤记录
     */
    Attendance saveAttendance(Attendance attendance);
    
    /**
     * 根据ID查询考勤记录
     * @param id 考勤记录ID
     * @return 考勤记录
     */
    Attendance getAttendanceById(Long id);
    
    /**
     * 查询所有考勤记录
     * @return 考勤记录列表
     */
    List<Attendance> getAllAttendance();
    
    /**
     * 分页查询所有考勤记录
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> getAllAttendance(Pageable pageable);
    
    /**
     * 根据工人ID查询考勤记录
     * @param workerId 工人ID
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> getAttendanceByWorker(Long workerId, Pageable pageable);
    
    /**
     * 根据日期范围查询考勤记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * 根据状态查询考勤记录
     * @param status 考勤状态
     * @param pageable 分页信息
     * @return 分页考勤记录列表
     */
    Page<Attendance> getAttendanceByStatus(String status, Pageable pageable);
    
    /**
     * 根据工人ID和日期查询考勤记录
     * @param workerId 工人ID
     * @param date 日期
     * @return 考勤记录（可能为空）
     */
    Attendance getAttendanceByWorkerAndDate(Long workerId, LocalDate date);
    
    /**
     * 删除考勤记录
     * @param id 考勤记录ID
     */
    void deleteAttendance(Long id);
    
    /**
     * 计算日期范围内的考勤记录数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 记录数量
     */
    long countAttendanceByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 计算日期范围内特定状态的考勤记录数量
     * @param status 考勤状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 记录数量
     */
    long countAttendanceByStatusAndDateRange(String status, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取日期范围内各工人的考勤统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工人考勤统计数据列表
     */
    List<Object[]> getWorkerAttendanceStatsByDateRange(LocalDate startDate, LocalDate endDate);
} 