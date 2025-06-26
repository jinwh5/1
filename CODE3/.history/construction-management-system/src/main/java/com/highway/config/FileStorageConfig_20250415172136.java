package com.highway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 文件存储配置
 */
@Slf4j
@Configuration
public class FileStorageConfig {

    @Value("${app.data.directory:data}")
    private String dataDirectory;

    /**
     * 初始化数据存储目录
     */
    @PostConstruct
    public void init() {
        createDirectoryIfNotExists(dataDirectory);
        log.info("数据存储目录初始化完成: {}", new File(dataDirectory).getAbsolutePath());
    }

    /**
     * 创建目录（如果不存在）
     */
    private void createDirectoryIfNotExists(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("已创建数据目录: {}", dirPath);
            } else {
                log.warn("无法创建数据目录: {}", dirPath);
            }
        }
    }
} 