package com.highway.service.interfaces;

import com.highway.model.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 工人服务接口
 */
public interface WorkerService {
    /**
     * 保存工人信息
     * @param worker 工人信息
     * @return 保存后的工人信息
     */
    Worker saveWorker(Worker worker);
    
    /**
     * 根据ID获取工人信息
     * @param id 工人ID
     * @return 工人信息
     */
    Worker getWorkerById(Long id);
    
    /**
     * 获取所有工人信息
     * @return 所有工人信息
     */
    List<Worker> getAllWorkers();
    
    /**
     * 获取所有工人信息（分页）
     * @param pageable 分页信息
     * @return 分页工人信息
     */
    Page<Worker> getAllWorkers(Pageable pageable);
    
    /**
     * 删除工人信息
     * @param id 工人ID
     */
    void deleteWorker(Long id);
    
    /**
     * 批量删除工人信息
     * @param ids 工人ID列表
     */
    void deleteWorkers(List<Long> ids);
    
    /**
     * 根据姓名查询工人（分页）
     * @param name 工人姓名
     * @param pageable 分页信息
     * @return 分页工人信息
     */
    Page<Worker> getWorkersByName(String name, Pageable pageable);
    
    /**
     * 根据职位查询工人
     * @param position 职位
     * @return 工人列表
     */
    List<Worker> getWorkersByPosition(String position);
    
    /**
     * 根据状态查询工人
     * @param status 状态
     * @return 工人列表
     */
    List<Worker> getWorkersByStatus(String status);
    
    /**
     * 根据身份证号查询工人
     * @param idCard 身份证号
     * @return 工人信息
     */
    Worker getWorkerByIdCard(String idCard);
} 