package com.highway.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static <T> void writeToFile(List<T> data, String filePath) throws IOException {
        File file = new File(filePath);
        // 确保父目录存在
        file.getParentFile().mkdirs();
        objectMapper.writeValue(file, data);
    }

    public static <T> List<T> readFromFile(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return objectMapper.readValue(file, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, clazz));
    }
} 