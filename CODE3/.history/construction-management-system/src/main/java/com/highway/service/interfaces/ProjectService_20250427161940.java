package com.highway.service.interfaces;

import com.highway.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 项目服务接口
 */
public interface ProjectService {
    /**
     * 保存项目信息
     * @param project 项目信息
     * @return 保存后的项目信息
     */
    Project saveProject(Project project);
    
    /**
     * 根据ID查询项目
     * @param id 项目ID
     * @return 项目信息
     */
    Project getProjectById(Long id);
    
    /**
     * 查询所有项目
     * @return 项目列表
     */
    List<Project> getAllProjects();
    
    /**
     * 分页查询所有项目
     * @param pageable 分页信息
     * @return 分页项目列表
     */
    Page<Project> getAllProjects(Pageable pageable);
    
    /**
     * 按名称查询项目（分页）
     * @param name 名称关键字
     * @param pageable 分页信息
     * @return 分页项目列表
     */
    Page<Project> getProjectsByName(String name, Pageable pageable);
    
    /**
     * 按状态查询项目
     * @param status 状态
     * @return 项目列表
     */
    List<Project> getProjectsByStatus(String status);
    
    /**
     * 删除项目
     * @param id 项目ID
     */
    void deleteProject(Long id);
} 