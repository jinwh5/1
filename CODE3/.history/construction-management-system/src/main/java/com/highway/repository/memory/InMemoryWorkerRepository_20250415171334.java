package com.highway.repository.memory;

import com.highway.model.Worker;
import com.highway.repository.interfaces.WorkerRepository;
import com.highway.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 工人信息仓库内存实现
 */
@Slf4j
@Repository
public class InMemoryWorkerRepository implements WorkerRepository {
    // 使用ConcurrentHashMap存储，保证线程安全
    private final ConcurrentMap<Long, Worker> workers = new ConcurrentHashMap<>();
    
    // 用于生成ID
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    // 数据文件路径
    private static final String DATA_FILE_PATH = "data/workers.json";
    
    @Override
    public Worker save(Worker worker) {
        // 如果是新增，生成ID
        if (worker.getId() == null) {
            worker.setId(idGenerator.getAndIncrement());
        }
        
        // 保存到内存
        workers.put(worker.getId(), worker);
        
        // 持久化到文件
        saveToFile();
        
        return worker;
    }
    
    @Override
    public Optional<Worker> findById(Long id) {
        return Optional.ofNullable(workers.get(id));
    }
    
    @Override
    public List<Worker> findAll() {
        return new ArrayList<>(workers.values());
    }
    
    @Override
    public Page<Worker> findAll(Pageable pageable) {
        List<Worker> workerList = new ArrayList<>(workers.values());
        return createPage(workerList, pageable);
    }
    
    @Override
    public Page<Worker> findByNameContaining(String name, Pageable pageable) {
        List<Worker> result = workers.values().stream()
                .filter(w -> w.getName() != null && w.getName().contains(name))
                .collect(Collectors.toList());
        return createPage(result, pageable);
    }
    
    @Override
    public Optional<Worker> findByIdCard(String idCard) {
        return workers.values().stream()
                .filter(w -> w.getIdCard() != null && w.getIdCard().equals(idCard))
                .findFirst();
    }
    
    @Override
    public List<Worker> findByPosition(String position) {
        return workers.values().stream()
                .filter(w -> w.getPosition() != null && w.getPosition().equals(position))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Worker> findByStatus(String status) {
        return workers.values().stream()
                .filter(w -> w.getStatus() != null && w.getStatus().equals(status))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        workers.remove(id);
        saveToFile();
    }
    
    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        ids.forEach(workers::remove);
        saveToFile();
    }
    
    /**
     * 创建分页结果
     */
    private Page<Worker> createPage(List<Worker> list, Pageable pageable) {
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
            JsonUtils.writeToFile(new ArrayList<>(workers.values()), DATA_FILE_PATH);
        } catch (IOException e) {
            log.error("保存工人数据到文件失败", e);
        }
    }
    
    /**
     * 应用启动时从文件加载数据
     */
    @PostConstruct
    public void loadFromFile() {
        try {
            List<Worker> loadedWorkers = JsonUtils.readFromFile(DATA_FILE_PATH, Worker.class);
            
            if (loadedWorkers != null && !loadedWorkers.isEmpty()) {
                loadedWorkers.forEach(w -> workers.put(w.getId(), w));
                
                // 更新ID生成器
                long maxId = workers.keySet().stream()
                        .mapToLong(Long::longValue)
                        .max()
                        .orElse(0);
                idGenerator.set(maxId + 1);
                
                log.info("已从文件加载{}条工人数据", loadedWorkers.size());
            }
        } catch (IOException e) {
            log.warn("从文件加载工人数据失败，将使用空数据", e);
        }
    }
} 