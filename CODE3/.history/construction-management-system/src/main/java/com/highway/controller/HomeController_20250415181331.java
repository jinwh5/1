package com.highway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 */
@Controller
public class HomeController {

    /**
     * 首页
     */
    @GetMapping("/")
    public String index(Model model) {
        // 添加一些示例数据到模型中
        model.addAttribute("personnelCount", 156);
        model.addAttribute("activePersonnelCount", 142);
        model.addAttribute("attendanceCount", 4568);
        model.addAttribute("salaryTotal", "1,258,600元");
        
        return "index";
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public String health() {
        return "health";
    }
} 