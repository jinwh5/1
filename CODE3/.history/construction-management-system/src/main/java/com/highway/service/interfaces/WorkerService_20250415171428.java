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
     * 根据ID查询工人
     * @param id 工人ID
     * @return 工人信息
     */
    Worker getWorkerById(Long id);
    
    /**
     * 查询所有工人
     * @return 工人列表
     */
    List<Worker> getAllWorkers();
    
    /**
     * 分页查询所有工人
     * @param pageable 分页信息
     * @return 分页工人列表
     */
    Page<Worker> getAllWorkers(Pageable pageable);
    
    /**
     * 按姓名查询工人（分页）
     * @param name 姓名关键字
     * @param pageable 分页信息
     * @return 分页工人列表
     */
    Page<Worker> getWorkersByName(String name, Pageable pageable);
    
    /**
     * 按身份证号查询工人
     * @param idCard 身份证号
     * @return 工人信息
     */
    Worker getWorkerByIdCard(String idCard);
    
    /**
     * 按职位查询工人
     * @param position 职位
     * @return 工人列表
     */
    List<Worker> getWorkersByPosition(String position);
    
    /**
     * 按状态查询工人
     * @param status 状态
     * @return 工人列表
     */
    List<Worker> getWorkersByStatus(String status);
    
    /**
     * 删除工人
     * @param id 工人ID
     */
    void deleteWorker(Long id);
    
    /**
     * 批量删除工人
     * @param ids ID列表
     */
    void deleteWorkers(List<Long> ids);
} 