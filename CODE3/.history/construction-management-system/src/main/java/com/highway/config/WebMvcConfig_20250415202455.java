package com.highway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

/**
 * Web MVC配置类
 * 用于配置全局日期时间格式化器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置全局日期时间格式化转换器
     */
    @Bean
    public FormattingConversionService mvcConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // 配置Java 8日期时间格式化器
        DateTimeFormatterRegistrar dateTimeRegistrar = new DateTimeFormatterRegistrar();
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        dateTimeRegistrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        dateTimeRegistrar.registerFormatters(conversionService);

        // 配置传统日期格式化器
        DateFormatterRegistrar dateRegistrar = new DateFormatterRegistrar();
        dateRegistrar.setFormatter(new DateFormatter("yyyy-MM-dd"));
        dateRegistrar.registerFormatters(conversionService);

        return conversionService;
    }
    
    /**
     * 配置静态资源处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
} 