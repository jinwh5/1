package com.highway.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成器，为不同类型的实体生成唯一ID
 */
public class IdGenerator {
    // 存储不同实体类型的ID生成器
    private static final ConcurrentMap<String, AtomicLong> GENERATORS = new ConcurrentHashMap<>();
    
    // 默认起始ID
    private static final long DEFAULT_INITIAL_VALUE = 1L;
    
    /**
     * 获取下一个ID
     *
     * @param entityType 实体类型
     * @return 生成的ID
     */
    public static Long nextId(String entityType) {
        return getGenerator(entityType).getAndIncrement();
    }
    
    /**
     * 设置ID生成器的当前值
     *
     * @param entityType 实体类型
     * @param currentValue 当前值
     */
    public static void setCurrentValue(String entityType, long currentValue) {
        getGenerator(entityType).set(currentValue);
    }
    
    /**
     * 获取ID生成器
     *
     * @param entityType 实体类型
     * @return ID生成器
     */
    private static AtomicLong getGenerator(String entityType) {
        return GENERATORS.computeIfAbsent(entityType, 
                key -> new AtomicLong(DEFAULT_INITIAL_VALUE));
    }
} 