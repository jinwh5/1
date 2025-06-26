package com.highway.repository.memory;

import com.highway.model.Progress;
import com.highway.repository.interfaces.ProgressRepository;
import com.highway.utils.JsonUtils;
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
 * 进度追踪内存存储实现
 */
@Repository
public class InMemoryProgressRepository implements ProgressRepository {
    private static final String DATA_FILE = "data/progress.json";
    private final ConcurrentHashMap<Long, Progress> progressMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            List<Progress> progressList = JsonUtils.readFromFile(DATA_FILE, Progress.class);
            progressList.forEach(progress -> progressMap.put(progress.getId(), progress));
        } catch (IOException e) {
            // 如果文件不存在，创建一个空文件
            saveToFile();
        }
    }

    private void saveToFile() {
        try {
            JsonUtils.writeToFile(new ArrayList<>(progressMap.values()), DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Progress save(Progress progress) {
        if (progress.getId() == null) {
            progress.setId(idGenerator.getAndIncrement());
        }
        progressMap.put(progress.getId(), progress);
        saveToFile();
        return progress;
    }

    @Override
    public Optional<Progress> findById(Long id) {
        return Optional.ofNullable(progressMap.get(id));
    }

    @Override
    public List<Progress> findAll() {
        return new ArrayList<>(progressMap.values());
    }

    @Override
    public Page<Progress> findAll(Pageable pageable) {
        List<Progress> allProgress = new ArrayList<>(progressMap.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allProgress.size());
        List<Progress> pageContent = allProgress.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allProgress.size());
    }

    @Override
    public List<Progress> findByProjectId(Long projectId) {
        return progressMap.values().stream()
                .filter(progress -> progress.getProjectId().equals(projectId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Progress> findByStatus(String status) {
        return progressMap.values().stream()
                .filter(progress -> progress.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Progress> findByPlannedEndDateBefore(LocalDate date) {
        return progressMap.values().stream()
                .filter(progress -> progress.getPlannedEndDate().isBefore(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Progress> findByActualProgressLessThanPlannedProgress() {
        return progressMap.values().stream()
                .filter(progress -> progress.getActualProgress() < progress.getPlannedProgress())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        progressMap.remove(id);
        saveToFile();
    }

    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        ids.forEach(progressMap::remove);
        saveToFile();
    }
} 