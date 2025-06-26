package com.highway.service.impl;

import com.highway.model.Worker;
import com.highway.repository.interfaces.WorkerRepository;
import com.highway.service.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 工人服务实现类（主要实现）
 */
@Service
@Primary
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    // 工人仓库
    private final WorkerRepository workerRepository;
    
    @Override
    public Worker saveWorker(Worker worker) {
        return workerRepository.save(worker);
    }
    
    @Override
    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("未找到ID为" + id + "的工人"));
    }
    
    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }
    
    @Override
    public Page<Worker> getAllWorkers(Pageable pageable) {
        return workerRepository.findAll(pageable);
    }
    
    @Override
    public Page<Worker> getWorkersByName(String name, Pageable pageable) {
        return workerRepository.findByNameContaining(name, pageable);
    }
    
    @Override
    public Worker getWorkerByIdCard(String idCard) {
        return workerRepository.findByIdCard(idCard)
                .orElse(null);
    }
    
    @Override
    public List<Worker> getWorkersByPosition(String position) {
        return workerRepository.findByPosition(position);
    }
    
    @Override
    public List<Worker> getWorkersByStatus(String status) {
        return workerRepository.findByStatus(status);
    }
    
    @Override
    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }
    
    @Override
    public void deleteWorkers(List<Long> ids) {
        workerRepository.deleteAllByIdIn(ids);
    }
} 