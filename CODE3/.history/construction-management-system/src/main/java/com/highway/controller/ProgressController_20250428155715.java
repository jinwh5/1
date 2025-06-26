package com.highway.controller;

import com.highway.model.Progress;
import com.highway.model.Project;
import com.highway.service.interfaces.ProgressService;
import com.highway.service.interfaces.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 进度追踪控制器
 */
@Controller
@RequestMapping("/progress")
public class ProgressController {
    private final ProgressService progressService;
    private final ProjectService projectService;

    public ProgressController(ProgressService progressService, ProjectService projectService) {
        this.progressService = progressService;
        this.projectService = projectService;
    }

    @GetMapping
    public String index(Model model, 
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size) {
        Page<Progress> progressPage = progressService.getAllProgress(PageRequest.of(page, size));
        model.addAttribute("progressList", progressPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", progressPage.getTotalPages());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("delayedProgress", progressService.getDelayedProgress());
        model.addAttribute("behindScheduleProgress", progressService.getBehindScheduleProgress());
        return "progress/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("progress", new Progress());
        model.addAttribute("projects", projectService.getAllProjects());
        return "progress/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Progress progress) {
        progress.setUpdateTime(LocalDateTime.now());
        progressService.saveProgress(progress);
        return "redirect:/progress";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("progress", progressService.getProgress(id));
        model.addAttribute("projects", projectService.getAllProjects());
        return "progress/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return "redirect:/progress";
    }

    @GetMapping("/project/{projectId}")
    public String projectProgress(@PathVariable Long projectId, Model model) {
        model.addAttribute("progressList", progressService.getProgressByProject(projectId));
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        return "progress/project";
    }
} 