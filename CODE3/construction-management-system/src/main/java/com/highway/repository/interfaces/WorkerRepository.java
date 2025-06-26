package com.highway.repository.interfaces;

import com.highway.model.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 工人信息仓库接口
 */
public interface WorkerRepository {
    /**
     * 保存工人信息
     * @param worker 工人信息
     * @return 保存后的工人信息
     */
    Worker save(Worker worker);
    
    /**
     * 根据ID查找工人
     * @param id 工人ID
     * @return 工人信息（可能为空）
     */
    Optional<Worker> findById(Long id);
    
    /**
     * 查询所有工人
     * @return 工人列表
     */
    List<Worker> findAll();
    
    /**
     * 分页查询所有工人
     * @param pageable 分页信息
     * @return 分页工人列表
     */
    Page<Worker> findAll(Pageable pageable);
    
    /**
     * 根据姓名模糊查询工人（分页）
     * @param name 姓名关键字
     * @param pageable 分页信息
     * @return 分页工人列表
     */
    Page<Worker> findByNameContaining(String name, Pageable pageable);
    
    /**
     * 根据身份证号查询工人
     * @param idCard 身份证号
     * @return 工人信息（可能为空）
     */
    Optional<Worker> findByIdCard(String idCard);
    
    /**
     * 根据职位查询工人列表
     * @param position 职位
     * @return 工人列表
     */
    List<Worker> findByPosition(String position);
    
    /**
     * 根据状态查询工人列表
     * @param status 状态
     * @return 工人列表
     */
    List<Worker> findByStatus(String status);
    
    /**
     * 删除工人信息
     * @param id 工人ID
     */
    void deleteById(Long id);
    
    /**
     * 批量删除工人信息
     * @param ids ID列表
     */
    void deleteAllByIdIn(List<Long> ids);
} 