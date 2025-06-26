package com.highway.service.impl;

import com.highway.model.Attendance;
import com.highway.repository.interfaces.AttendanceRepository;
import com.highway.service.interfaces.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 考勤服务实现类
 */
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    // 考勤仓库
    private final AttendanceRepository attendanceRepository;
    
    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }
    
    @Override
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("未找到ID为" + id + "的考勤记录"));
    }
    
    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    @Override
    public Page<Attendance> getAllAttendance(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }
    
    @Override
    public Page<Attendance> getAttendanceByWorker(Long workerId, Pageable pageable) {
        return attendanceRepository.findByWorkerId(workerId, pageable);
    }
    
    @Override
    public Page<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return attendanceRepository.findByDateBetween(startDate, endDate, pageable);
    }
    
    @Override
    public Page<Attendance> getAttendanceByStatus(String status, Pageable pageable) {
        return attendanceRepository.findByStatus(status, pageable);
    }
    
    @Override
    public Attendance getAttendanceByWorkerAndDate(Long workerId, LocalDate date) {
        return attendanceRepository.findByWorkerIdAndDate(workerId, date)
                .orElse(null);
    }
    
    @Override
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
    
    @Override
    public long countAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.countByDateBetween(startDate, endDate);
    }
    
    @Override
    public long countAttendanceByStatusAndDateRange(String status, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.countByStatusAndDateBetween(status, startDate, endDate);
    }
    
    @Override
    public List<Object[]> getWorkerAttendanceStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        // 在实际项目中，这里通常会调用存储过程或复杂查询
        // 这里简化处理，返回模拟数据
        List<Object[]> stats = new ArrayList<>();
        
        // 获取日期范围内的所有考勤记录
        List<Attendance> allAttendance = attendanceRepository.findByDateBetween(startDate, endDate);
        
        // 在实际项目中，这里应该进行数据统计分析
        // 简化示例，将来可以通过实际数据库查询或者数据处理逻辑完善
        
        return stats;
    }
} 