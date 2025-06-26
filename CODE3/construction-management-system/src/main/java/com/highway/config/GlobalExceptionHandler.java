package com.highway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源不存在的异常
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(NoSuchElementException e, HttpServletRequest request) {
        log.error("请求: {} 发生错误: {}", request.getRequestURI(), e.getMessage());
        
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", "未找到请求的资源");
        mav.addObject("moduleName", extractModuleName(request.getRequestURI()));
        return mav;
    }
    
    /**
     * 处理Thymeleaf模板处理异常
     */
    @ExceptionHandler({TemplateProcessingException.class, TemplateInputException.class})
    public ModelAndView handleTemplateException(Exception e, HttpServletRequest request) {
        log.error("模板处理错误: {} 请求: {}", e.getMessage(), request.getRequestURI());
        
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "页面渲染出错，请联系管理员");
        mav.addObject("errorDetails", e.getMessage());
        mav.addObject("requestUri", request.getRequestURI());
        mav.addObject("moduleName", extractModuleName(request.getRequestURI()));
        return mav;
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request) {
        log.error("请求: {} 发生错误: {}", request.getRequestURI(), e.getMessage(), e);
        
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "处理请求时发生错误");
        mav.addObject("errorDetails", e.getMessage());
        mav.addObject("requestUri", request.getRequestURI());
        mav.addObject("moduleName", extractModuleName(request.getRequestURI()));
        return mav;
    }
    
    /**
     * 从请求URI中提取模块名称
     */
    private String extractModuleName(String uri) {
        if (uri == null || uri.isEmpty() || uri.equals("/")) {
            return "首页";
        }
        
        String[] parts = uri.split("/");
        if (parts.length > 1) {
            String moduleName = parts[1];
            
            // 转换为用户友好的名称
            switch (moduleName) {
                case "workers": return "工人管理";
                case "attendance": return "考勤管理";
                case "projects": return "项目管理";
                case "equipment": return "设备管理";
                case "safety": return "安全管理";
                case "system": return "系统设置";
                default: return moduleName;
            }
        }
        
        return "未知模块";
    }
} 