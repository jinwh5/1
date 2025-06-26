package com.highway.repository.interfaces;

import com.highway.model.Progress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 进度追踪数据访问接口
 */
public interface ProgressRepository {
    Progress save(Progress progress);
    Optional<Progress> findById(Long id);
    List<Progress> findAll();
    Page<Progress> findAll(Pageable pageable);
    List<Progress> findByProjectId(Long projectId);
    List<Progress> findByStatus(String status);
    List<Progress> findByPlannedEndDateBefore(LocalDate date);
    List<Progress> findByActualProgressLessThanPlannedProgress();
    void deleteById(Long id);
    void deleteAllByIdIn(List<Long> ids);
} 