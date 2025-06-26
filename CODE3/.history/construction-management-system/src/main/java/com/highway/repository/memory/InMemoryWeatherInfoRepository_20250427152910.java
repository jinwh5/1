package com.highway.repository.memory;

import com.highway.model.WeatherInfo;
import com.highway.repository.interfaces.WeatherInfoRepository;
import com.highway.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 天气信息仓库内存实现
 */
@Slf4j
@Repository
public class InMemoryWeatherInfoRepository implements WeatherInfoRepository {
    // 使用ConcurrentHashMap存储，保证线程安全
    private final ConcurrentMap<Long, WeatherInfo> weatherInfos = new ConcurrentHashMap<>();
    
    // 用于生成ID
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    // 数据文件路径
    private static final String DATA_FILE_PATH = "data/weather_infos.json";
    
    @Override
    public WeatherInfo save(WeatherInfo weatherInfo) {
        // 如果是新增，生成ID
        if (weatherInfo.getId() == null) {
            weatherInfo.setId(idGenerator.getAndIncrement());
        }
        
        // 保存到内存
        weatherInfos.put(weatherInfo.getId(), weatherInfo);
        
        // 持久化到文件
        saveToFile();
        
        return weatherInfo;
    }
    
    @Override
    public Optional<WeatherInfo> findById(Long id) {
        return Optional.ofNullable(weatherInfos.get(id));
    }
    
    @Override
    public List<WeatherInfo> findAll() {
        return new ArrayList<>(weatherInfos.values());
    }
    
    @Override
    public Page<WeatherInfo> findAll(Pageable pageable) {
        List<WeatherInfo> weatherInfoList = new ArrayList<>(weatherInfos.values());
        return createPage(weatherInfoList, pageable);
    }
    
    @Override
    public Optional<WeatherInfo> findByLocationAndDate(String location, LocalDate date) {
        return weatherInfos.values().stream()
                .filter(w -> w.getLocation().equals(location) && w.getDate().equals(date))
                .findFirst();
    }
    
    @Override
    public List<WeatherInfo> findByLocationAndDateBetween(String location, LocalDate startDate, LocalDate endDate) {
        return weatherInfos.values().stream()
                .filter(w -> w.getLocation().equals(location))
                .filter(w -> !w.getDate().isBefore(startDate) && !w.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        weatherInfos.remove(id);
        saveToFile();
    }
    
    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        ids.forEach(weatherInfos::remove);
        saveToFile();
    }
    
    /**
     * 创建分页结果
     */
    private Page<WeatherInfo> createPage(List<WeatherInfo> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        
        // 避免索引越界
        if (start > list.size()) {
            start = 0;
            end = 0;
        }
        
        return new PageImpl<>(
                list.subList(start, end), 
                pageable, 
                list.size()
        );
    }
    
    /**
     * 保存数据到文件
     */
    private void saveToFile() {
        try {
            JsonUtils.writeToFile(new ArrayList<>(weatherInfos.values()), DATA_FILE_PATH);
        } catch (IOException e) {
            log.error("保存天气数据到文件失败", e);
        }
    }
    
    /**
     * 应用启动时从文件加载数据
     */
    @PostConstruct
    public void loadFromFile() {
        try {
            List<WeatherInfo> loadedWeatherInfos = JsonUtils.readFromFile(DATA_FILE_PATH, WeatherInfo.class);
            
            if (loadedWeatherInfos != null && !loadedWeatherInfos.isEmpty()) {
                loadedWeatherInfos.forEach(w -> weatherInfos.put(w.getId(), w));
                
                // 更新ID生成器
                long maxId = weatherInfos.keySet().stream()
                        .mapToLong(Long::longValue)
                        .max()
                        .orElse(0);
                idGenerator.set(maxId + 1);
                
                log.info("已从文件加载{}条天气数据", loadedWeatherInfos.size());
            }
        } catch (IOException e) {
            log.warn("从文件加载天气数据失败，将使用空数据", e);
        }
    }
} 