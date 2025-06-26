package com.highway.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON工具类，用于序列化和反序列化JSON数据
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    static {
        // 注册Java 8时间模块
        mapper.registerModule(new JavaTimeModule());
        // 配置日期格式化
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
    /**
     * 将对象写入JSON文件
     *
     * @param data     要保存的数据
     * @param filePath 文件路径
     * @throws IOException IO异常
     */
    public static <T> void writeToFile(List<T> data, String filePath) throws IOException {
        File file = new File(filePath);
        
        // 确保父目录存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        
        // 写入文件
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }
    
    /**
     * 从JSON文件读取对象列表
     *
     * @param filePath 文件路径
     * @param type     对象类型
     * @return 对象列表
     * @throws IOException IO异常
     */
    public static <T> List<T> readFromFile(String filePath, Class<T> type) throws IOException {
        File file = new File(filePath);
        
        // 如果文件不存在，返回空列表
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        // 构建集合类型
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, type);
        
        // 读取文件
        return mapper.readValue(file, listType);
    }
    
    /**
     * 从JSON文件读取复杂类型的对象列表
     *
     * @param filePath     文件路径
     * @param typeReference 类型引用
     * @return 对象列表
     * @throws IOException IO异常
     */
    public static <T> List<T> readFromFile(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        File file = new File(filePath);
        
        // 如果文件不存在，返回空列表
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        // 读取文件
        return mapper.readValue(file, typeReference);
    }
} 