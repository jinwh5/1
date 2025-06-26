package com.highway.service.memory;

import com.highway.model.Worker;
import com.highway.repository.interfaces.WorkerRepository;
import com.highway.service.interfaces.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工人服务内存实现类
 */
@Service
public class InMemoryWorkerServiceImpl implements WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public Worker saveWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    @Override
    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id).orElse(null);
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
    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }

    @Override
    public void deleteWorkers(List<Long> ids) {
        // 遍历ID列表，逐个删除
        for (Long id : ids) {
            workerRepository.deleteById(id);
        }
    }

    @Override
    public Page<Worker> getWorkersByName(String name, Pageable pageable) {
        return workerRepository.findByNameContaining(name, pageable);
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
    public Worker getWorkerByIdCard(String idCard) {
        return workerRepository.findByIdCard(idCard).orElse(null);
    }
} 