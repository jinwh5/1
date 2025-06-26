package com.highway.controller;

import com.highway.model.Worker;
import com.highway.service.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 工人控制器
 */
@Controller
@RequestMapping("/workers")
@RequiredArgsConstructor
public class WorkerController {
    // 工人服务
    private final WorkerService workerService;
    
    /**
     * 工人列表页面
     */
    @GetMapping
    public String listWorkers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String status,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Worker> workerPage;
        
        // 根据条件查询
        if (name != null && !name.isEmpty()) {
            workerPage = workerService.getWorkersByName(name, pageable);
            model.addAttribute("name", name);
        } else {
            workerPage = workerService.getAllWorkers(pageable);
        }
        
        // 添加额外的过滤条件（在内存中过滤，不影响分页）
        if (position != null && !position.isEmpty()) {
            model.addAttribute("position", position);
        }
        if (status != null && !status.isEmpty()) {
            model.addAttribute("status", status);
        }
        
        model.addAttribute("workers", workerPage.getContent());
        model.addAttribute("currentPage", workerPage.getNumber());
        model.addAttribute("totalPages", workerPage.getTotalPages());
        model.addAttribute("totalItems", workerPage.getTotalElements());
        
        return "worker/list";
    }
    
    /**
     * 添加工人表单页面
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("worker", new Worker());
        return "worker/form";
    }
    
    /**
     * 编辑工人表单页面
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        model.addAttribute("worker", worker);
        return "worker/form";
    }
    
    /**
     * 保存工人信息
     */
    @PostMapping("/save")
    public String saveWorker(Worker worker, RedirectAttributes redirectAttributes) {
        workerService.saveWorker(worker);
        redirectAttributes.addFlashAttribute("message", "工人信息保存成功");
        return "redirect:/workers";
    }
    
    /**
     * 显示工人详情
     */
    @GetMapping("/view/{id}")
    public String viewWorker(@PathVariable Long id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        model.addAttribute("worker", worker);
        return "worker/view";
    }
    
    /**
     * 删除工人
     */
    @GetMapping("/delete/{id}")
    public String deleteWorker(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workerService.deleteWorker(id);
        redirectAttributes.addFlashAttribute("message", "工人信息删除成功");
        return "redirect:/workers";
    }
    
    /**
     * 批量删除工人
     */
    @PostMapping("/delete-batch")
    public String deleteWorkers(@RequestParam List<Long> ids, RedirectAttributes redirectAttributes) {
        workerService.deleteWorkers(ids);
        redirectAttributes.addFlashAttribute("message", "已删除" + ids.size() + "条工人信息");
        return "redirect:/workers";
    }
} 