package com.highway.controller;

import org.springframework.stereotype.Controller;
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
    public String index() {
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