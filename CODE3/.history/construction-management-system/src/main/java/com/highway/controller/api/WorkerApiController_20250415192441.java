package com.highway.controller.api;

import com.highway.model.Worker;
import com.highway.service.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工人API控制器
 * 提供工人数据的REST接口
 */
@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerApiController {
    
    private final WorkerService workerService;
    
    /**
     * 获取所有工人列表（分页）
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param name 工人姓名（可选）
     * @param position 职位（可选）
     * @param status 状态（可选）
     * @return 工人列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllWorkers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String status) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Worker> workerPage;
        
        // 根据条件查询
        if (name != null && !name.isEmpty()) {
            workerPage = workerService.getWorkersByName(name, pageable);
        } else {
            workerPage = workerService.getAllWorkers(pageable);
        }
        
        // 准备响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("workers", workerPage.getContent());
        response.put("currentPage", workerPage.getNumber());
        response.put("totalItems", workerPage.getTotalElements());
        response.put("totalPages", workerPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取工人信息
     * @param id 工人ID
     * @return 工人信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long id) {
        try {
            Worker worker = workerService.getWorkerById(id);
            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建新工人
     * @param worker 工人信息
     * @return 创建的工人信息
     */
    @PostMapping
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) {
        try {
            Worker savedWorker = workerService.saveWorker(worker);
            return new ResponseEntity<>(savedWorker, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新工人信息
     * @param id 工人ID
     * @param worker 工人信息
     * @return 更新后的工人信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable Long id, @RequestBody Worker worker) {
        try {
            worker.setId(id);  // 确保ID正确
            Worker updatedWorker = workerService.saveWorker(worker);
            return ResponseEntity.ok(updatedWorker);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除工人
     * @param id 工人ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteWorker(@PathVariable Long id) {
        try {
            workerService.deleteWorker(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "工人删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 批量删除工人
     * @param ids 工人ID列表
     * @return 操作结果
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteWorkers(@RequestBody List<Long> ids) {
        try {
            workerService.deleteWorkers(ids);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "已删除" + ids.size() + "条工人信息");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 按职位获取工人
     * @param position 职位
     * @return 工人列表
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Worker>> getWorkersByPosition(@PathVariable String position) {
        List<Worker> workers = workerService.getWorkersByPosition(position);
        return ResponseEntity.ok(workers);
    }
    
    /**
     * 按状态获取工人
     * @param status 状态
     * @return 工人列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Worker>> getWorkersByStatus(@PathVariable String status) {
        List<Worker> workers = workerService.getWorkersByStatus(status);
        return ResponseEntity.ok(workers);
    }
    
    /**
     * 根据身份证号查询工人
     * @param idCard 身份证号
     * @return 工人信息
     */
    @GetMapping("/id-card/{idCard}")
    public ResponseEntity<Worker> getWorkerByIdCard(@PathVariable String idCard) {
        try {
            Worker worker = workerService.getWorkerByIdCard(idCard);
            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取工人统计信息
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getWorkerStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        List<Worker> allWorkers = workerService.getAllWorkers();
        statistics.put("totalWorkers", allWorkers.size());
        
        // 按职位分组统计
        Map<String, Long> positionStats = new HashMap<>();
        allWorkers.stream()
                .filter(w -> w.getPosition() != null)
                .forEach(w -> {
                    positionStats.put(w.getPosition(), 
                            positionStats.getOrDefault(w.getPosition(), 0L) + 1);
                });
        statistics.put("positionStats", positionStats);
        
        // 按状态分组统计
        Map<String, Long> statusStats = new HashMap<>();
        allWorkers.stream()
                .filter(w -> w.getStatus() != null)
                .forEach(w -> {
                    statusStats.put(w.getStatus(), 
                            statusStats.getOrDefault(w.getStatus(), 0L) + 1);
                });
        statistics.put("statusStats", statusStats);
        
        return ResponseEntity.ok(statistics);
    }
} 