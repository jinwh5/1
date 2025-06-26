package com.highway.service.impl;

import com.highway.model.Progress;
import com.highway.repository.interfaces.ProgressRepository;
import com.highway.service.interfaces.ProgressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 进度追踪服务实现类
 */
@Service
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    @Override
    public Progress getProgress(Long id) {
        return progressRepository.findById(id).orElse(null);
    }

    @Override
    public List<Progress> getAllProgress() {
        return progressRepository.findAll();
    }

    @Override
    public Page<Progress> getAllProgress(Pageable pageable) {
        return progressRepository.findAll(pageable);
    }

    @Override
    public List<Progress> getProgressByProject(Long projectId) {
        return progressRepository.findByProjectId(projectId);
    }

    @Override
    public List<Progress> getProgressByStatus(String status) {
        return progressRepository.findByStatus(status);
    }

    @Override
    public List<Progress> getDelayedProgress() {
        return progressRepository.findByPlannedEndDateBefore(LocalDate.now());
    }

    @Override
    public List<Progress> getBehindScheduleProgress() {
        return progressRepository.findByActualProgressLessThanPlannedProgress();
    }

    @Override
    public void deleteProgress(Long id) {
        progressRepository.deleteById(id);
    }

    @Override
    public void deleteProgress(List<Long> ids) {
        progressRepository.deleteAllByIdIn(ids);
    }

    @Override
    public boolean isProgressDelayed(Progress progress) {
        return progress.getActualProgress() < progress.getPlannedProgress() ||
               (progress.getPlannedEndDate() != null && 
                progress.getPlannedEndDate().isBefore(LocalDate.now()) &&
                !"已完成".equals(progress.getStatus()));
    }

    @Override
    public String getProgressStatus(Progress progress) {
        if ("已完成".equals(progress.getStatus())) {
            return "已完成";
        }
        if (isProgressDelayed(progress)) {
            return "已延期";
        }
        return "进行中";
    }
} 