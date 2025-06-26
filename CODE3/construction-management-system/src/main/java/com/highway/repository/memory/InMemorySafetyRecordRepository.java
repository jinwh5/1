package com.highway.repository.memory;

import com.highway.model.SafetyRecord;
import com.highway.repository.interfaces.SafetyRecordRepository;
import com.highway.util.JsonUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 安全监控记录内存存储实现
 */
@Repository
public class InMemorySafetyRecordRepository implements SafetyRecordRepository {
    private final ConcurrentHashMap<Long, SafetyRecord> records = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private static final String DATA_FILE = "data/safety_records.json";

    @PostConstruct
    public void init() {
        loadFromFile();
    }

    @Override
    public SafetyRecord save(SafetyRecord record) {
        if (record.getId() == null) {
            record.setId(idGenerator.getAndIncrement());
        }
        records.put(record.getId(), record);
        saveToFile();
        return record;
    }

    @Override
    public Optional<SafetyRecord> findById(Long id) {
        return Optional.ofNullable(records.get(id));
    }

    @Override
    public List<SafetyRecord> findAll() {
        return new ArrayList<>(records.values());
    }

    @Override
    public Page<SafetyRecord> findAll(Pageable pageable) {
        List<SafetyRecord> allRecords = findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allRecords.size());
        return new PageImpl<>(
            allRecords.subList(start, end),
            pageable,
            allRecords.size()
        );
    }

    @Override
    public List<SafetyRecord> findByWorkerId(Long workerId) {
        return records.values().stream()
            .filter(record -> record.getWorkerId().equals(workerId))
            .collect(Collectors.toList());
    }

    @Override
    public List<SafetyRecord> findByProjectId(Long projectId) {
        return records.values().stream()
            .filter(record -> record.getProjectId().equals(projectId))
            .collect(Collectors.toList());
    }

    @Override
    public List<SafetyRecord> findByEventType(String eventType) {
        return records.values().stream()
            .filter(record -> record.getEventType().equals(eventType))
            .collect(Collectors.toList());
    }

    @Override
    public List<SafetyRecord> findBySeverityLevel(String severityLevel) {
        return records.values().stream()
            .filter(record -> record.getSeverityLevel().equals(severityLevel))
            .collect(Collectors.toList());
    }

    @Override
    public List<SafetyRecord> findByStatus(String status) {
        return records.values().stream()
            .filter(record -> record.getStatus().equals(status))
            .collect(Collectors.toList());
    }

    @Override
    public List<SafetyRecord> findByOccurrenceTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return records.values().stream()
            .filter(record -> !record.getOccurrenceTime().isBefore(startTime) 
                && !record.getOccurrenceTime().isAfter(endTime))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        records.remove(id);
        saveToFile();
    }

    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        ids.forEach(records::remove);
        saveToFile();
    }

    private void saveToFile() {
        try {
            JsonUtils.writeToFile(new ArrayList<>(records.values()), DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try {
            List<SafetyRecord> loadedRecords = JsonUtils.readFromFile(DATA_FILE, SafetyRecord.class);
            if (loadedRecords != null) {
                records.clear();
                loadedRecords.forEach(record -> records.put(record.getId(), record));
                // 更新ID生成器的值为最大ID + 1
                long maxId = records.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
                idGenerator.set(maxId + 1);
            }
        } catch (Exception e) {
            // 如果文件不存在或读取失败，使用空的数据集
            records.clear();
            idGenerator.set(1);
        }
    }
} 