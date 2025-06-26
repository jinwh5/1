package com.highway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, 
                                              HttpServletRequest request,
                                              RedirectAttributes redirectAttributes) {
        log.error("资源未找到: {}", e.getMessage());
        
        // 从请求URL中提取模块名称
        String requestURI = request.getRequestURI();
        String moduleName = extractModuleName(requestURI);
        
        redirectAttributes.addFlashAttribute("error", "未找到请求的资源: " + e.getMessage());
        return "redirect:/" + moduleName;
    }
    
    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("发生异常: ", e);
        model.addAttribute("error", "系统错误: " + e.getMessage());
        model.addAttribute("timestamp", System.currentTimeMillis());
        model.addAttribute("path", "N/A");
        model.addAttribute("status", 500);
        return "error";
    }
    
    /**
     * 从请求URI中提取模块名称
     */
    private String extractModuleName(String requestURI) {
        // 移除开头的斜杠
        String path = requestURI.startsWith("/") ? requestURI.substring(1) : requestURI;
        
        // 获取第一段路径
        int slashIndex = path.indexOf("/");
        if (slashIndex > 0) {
            return path.substring(0, slashIndex);
        }
        
        // 如果没有斜杠，返回整个路径或默认为"home"
        return path.isEmpty() ? "home" : path;
    }
} 