package com.highway.service.interfaces;

import com.highway.model.Progress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

/**
 * 进度追踪服务接口
 */
public interface ProgressService {
    Progress saveProgress(Progress progress);
    Progress getProgress(Long id);
    List<Progress> getAllProgress();
    Page<Progress> getAllProgress(Pageable pageable);
    List<Progress> getProgressByProject(Long projectId);
    List<Progress> getProgressByStatus(String status);
    List<Progress> getDelayedProgress();
    List<Progress> getBehindScheduleProgress();
    void deleteProgress(Long id);
    void deleteProgress(List<Long> ids);
    boolean isProgressDelayed(Progress progress);
    String getProgressStatus(Progress progress);
} 