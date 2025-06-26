package com.highway.repository.memory;

import com.highway.model.Attendance;
import com.highway.repository.interfaces.AttendanceRepository;
import com.highway.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 考勤信息内存仓库实现
 */
@Slf4j
@Repository
public class InMemoryAttendanceRepository implements AttendanceRepository {
    private static final String DATA_FILE_PATH = "data/attendance.json";
    
    // 内存中存储考勤记录的Map
    private final Map<Long, Attendance> attendances = new ConcurrentHashMap<>();
    
    // ID生成器
    private final AtomicLong idGenerator = new AtomicLong(0);
    
    @PostConstruct
    public void init() {
        try {
            // 从文件加载数据
            List<Attendance> loadedAttendances = JsonUtils.readFromFile(DATA_FILE_PATH, Attendance.class);
            if (loadedAttendances != null) {
                // 初始化ID生成器
                long maxId = 0;
                for (Attendance attendance : loadedAttendances) {
                    attendances.put(attendance.getId(), attendance);
                    maxId = Math.max(maxId, attendance.getId());
                }
                idGenerator.set(maxId);
                log.info("从文件加载了{}条考勤记录", loadedAttendances.size());
            }
        } catch (IOException e) {
            log.warn("无法从文件加载考勤数据: {}", e.getMessage());
        }
    }
    
    @Override
    public Attendance save(Attendance attendance) {
        if (attendance.getId() == null) {
            // 新增记录，生成ID
            attendance.setId(idGenerator.incrementAndGet());
        }
        attendances.put(attendance.getId(), attendance);
        saveToFile();
        return attendance;
    }
    
    @Override
    public Optional<Attendance> findById(Long id) {
        return Optional.ofNullable(attendances.get(id));
    }
    
    @Override
    public List<Attendance> findAll() {
        return new ArrayList<>(attendances.values());
    }
    
    @Override
    public Page<Attendance> findAll(Pageable pageable) {
        List<Attendance> allAttendances = new ArrayList<>(attendances.values());
        // 根据日期降序排序
        allAttendances.sort((a1, a2) -> a2.getDate().compareTo(a1.getDate()));
        return createPage(allAttendances, pageable);
    }
    
    @Override
    public Page<Attendance> findByWorkerId(Long workerId, Pageable pageable) {
        List<Attendance> result = attendances.values().stream()
                .filter(a -> a.getWorkerId().equals(workerId))
                .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate()))
                .collect(Collectors.toList());
        return createPage(result, pageable);
    }
    
    @Override
    public Page<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Attendance> result = attendances.values().stream()
                .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
                .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate()))
                .collect(Collectors.toList());
        return createPage(result, pageable);
    }
    
    @Override
    public List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return attendances.values().stream()
                .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
                .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Attendance> findByStatus(String status, Pageable pageable) {
        List<Attendance> result = attendances.values().stream()
                .filter(a -> a.getStatus().equals(status))
                .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate()))
                .collect(Collectors.toList());
        return createPage(result, pageable);
    }
    
    @Override
    public Optional<Attendance> findByWorkerIdAndDate(Long workerId, LocalDate date) {
        return attendances.values().stream()
                .filter(a -> a.getWorkerId().equals(workerId) && a.getDate().equals(date))
                .findFirst();
    }
    
    @Override
    public void deleteById(Long id) {
        attendances.remove(id);
        saveToFile();
    }
    
    @Override
    public long countByDateBetween(LocalDate startDate, LocalDate endDate) {
        return attendances.values().stream()
                .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
                .count();
    }
    
    @Override
    public long countByStatusAndDateBetween(String status, LocalDate startDate, LocalDate endDate) {
        return attendances.values().stream()
                .filter(a -> a.getStatus().equals(status) 
                        && !a.getDate().isBefore(startDate) 
                        && !a.getDate().isAfter(endDate))
                .count();
    }
    
    /**
     * 创建分页结果
     */
    private Page<Attendance> createPage(List<Attendance> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        
        // 避免索引越界
        if (start > list.size()) {
            start = 0;
            end = 0;
        }
        
        return new PageImpl<>(
                list.subList(start, end), 
                pageable, 
                list.size()
        );
    }
    
    /**
     * 保存数据到文件
     */
    private void saveToFile() {
        try {
            JsonUtils.writeToFile(new ArrayList<>(attendances.values()), DATA_FILE_PATH);
        } catch (IOException e) {
            log.error("保存考勤数据到文件失败", e);
        }
    }
} 