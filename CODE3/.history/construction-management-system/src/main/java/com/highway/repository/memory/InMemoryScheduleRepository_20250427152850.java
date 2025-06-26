package com.highway.repository.memory;

import com.highway.model.Schedule;
import com.highway.repository.interfaces.ScheduleRepository;
import com.highway.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 排班信息仓库内存实现
 */
@Slf4j
@Repository
public class InMemoryScheduleRepository implements ScheduleRepository {
    // 使用ConcurrentHashMap存储，保证线程安全
    private final ConcurrentMap<Long, Schedule> schedules = new ConcurrentHashMap<>();
    
    // 用于生成ID
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    // 数据文件路径
    private static final String DATA_FILE_PATH = "data/schedules.json";
    
    @Override
    public Schedule save(Schedule schedule) {
        // 如果是新增，生成ID
        if (schedule.getId() == null) {
            schedule.setId(idGenerator.getAndIncrement());
        }
        
        // 保存到内存
        schedules.put(schedule.getId(), schedule);
        
        // 持久化到文件
        saveToFile();
        
        return schedule;
    }
    
    @Override
    public Optional<Schedule> findById(Long id) {
        return Optional.ofNullable(schedules.get(id));
    }
    
    @Override
    public List<Schedule> findAll() {
        return new ArrayList<>(schedules.values());
    }
    
    @Override
    public Page<Schedule> findAll(Pageable pageable) {
        List<Schedule> scheduleList = new ArrayList<>(schedules.values());
        return createPage(scheduleList, pageable);
    }
    
    @Override
    public List<Schedule> findByWorkerId(Long workerId) {
        return schedules.values().stream()
                .filter(s -> s.getWorkerId().equals(workerId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Schedule> findByWorkerIdAndDateBetween(Long workerId, LocalDate startDate, LocalDate endDate) {
        return schedules.values().stream()
                .filter(s -> s.getWorkerId().equals(workerId))
                .filter(s -> !s.getDate().isBefore(startDate) && !s.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Schedule> findByProjectId(Long projectId) {
        return schedules.values().stream()
                .filter(s -> s.getProjectId().equals(projectId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Schedule> findByProjectIdAndDateBetween(Long projectId, LocalDate startDate, LocalDate endDate) {
        return schedules.values().stream()
                .filter(s -> s.getProjectId().equals(projectId))
                .filter(s -> !s.getDate().isBefore(startDate) && !s.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Schedule> findByDateAndLocation(LocalDate date, String location) {
        return schedules.values().stream()
                .filter(s -> s.getDate().equals(date))
                .filter(s -> s.getLocation().equals(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        schedules.remove(id);
        saveToFile();
    }
    
    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        ids.forEach(schedules::remove);
        saveToFile();
    }
    
    /**
     * 创建分页结果
     */
    private Page<Schedule> createPage(List<Schedule> list, Pageable pageable) {
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
            JsonUtils.writeToFile(new ArrayList<>(schedules.values()), DATA_FILE_PATH);
        } catch (IOException e) {
            log.error("保存排班数据到文件失败", e);
        }
    }
    
    /**
     * 应用启动时从文件加载数据
     */
    @PostConstruct
    public void loadFromFile() {
        try {
            List<Schedule> loadedSchedules = JsonUtils.readFromFile(DATA_FILE_PATH, Schedule.class);
            
            if (loadedSchedules != null && !loadedSchedules.isEmpty()) {
                loadedSchedules.forEach(s -> schedules.put(s.getId(), s));
                
                // 更新ID生成器
                long maxId = schedules.keySet().stream()
                        .mapToLong(Long::longValue)
                        .max()
                        .orElse(0);
                idGenerator.set(maxId + 1);
                
                log.info("已从文件加载{}条排班数据", loadedSchedules.size());
            }
        } catch (IOException e) {
            log.warn("从文件加载排班数据失败，将使用空数据", e);
        }
    }
} 