package com.highway.service.impl;

import com.highway.model.WeatherInfo;
import com.highway.repository.interfaces.WeatherInfoRepository;
import com.highway.service.interfaces.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

/**
 * 天气服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherInfoRepository weatherInfoRepository;
    
    // 模拟天气API的配置
    @Value("${app.weather.simulation-enabled:true}")
    private boolean simulationEnabled;
    
    // 模拟天气数据
    private final Random random = new Random();
    private final String[] weatherConditions = {"晴", "多云", "阴", "小雨", "中雨", "大雨", "雷阵雨"};
    
    @Override
    public WeatherInfo getWeatherInfo(String location, LocalDate date) {
        // 先从仓库中查找
        return weatherInfoRepository.findByLocationAndDate(location, date)
                .orElseGet(() -> {
                    // 如果不存在，则模拟生成
                    WeatherInfo weatherInfo = simulateWeatherInfo(location, date);
                    // 保存到仓库
                    return weatherInfoRepository.save(weatherInfo);
                });
    }
    
    @Override
    public WeatherInfo updateWeatherInfo(String location) {
        return getWeatherInfo(location, LocalDate.now());
    }
    
    @Override
    public boolean isSuitableForWork(WeatherInfo weatherInfo) {
        // 不适合施工的条件：
        // 1. 降雨量超过5mm
        // 2. 风速超过10m/s
        // 3. 温度低于5度或高于35度
        if (weatherInfo.getRainfall() > 5.0 ||
            weatherInfo.getWindSpeed() > 10.0 ||
            weatherInfo.getTemperature() < 5.0 ||
            weatherInfo.getTemperature() > 35.0) {
            return false;
        }
        return true;
    }
    
    @Override
    public String getWorkSuggestion(WeatherInfo weatherInfo) {
        StringBuilder suggestion = new StringBuilder();
        
        // 根据天气状况给出建议
        if (weatherInfo.getRainfall() > 0) {
            suggestion.append("有降雨，请做好防雨措施。");
        }
        if (weatherInfo.getWindSpeed() > 5.0) {
            suggestion.append("风速较大，请注意高空作业安全。");
        }
        if (weatherInfo.getTemperature() < 10.0) {
            suggestion.append("气温较低，请做好保暖。");
        } else if (weatherInfo.getTemperature() > 30.0) {
            suggestion.append("气温较高，请注意防暑降温。");
        }
        
        if (suggestion.length() == 0) {
            suggestion.append("天气适合施工，请正常作业。");
        }
        
        return suggestion.toString();
    }
    
    /**
     * 模拟生成天气信息
     */
    private WeatherInfo simulateWeatherInfo(String location, LocalDate date) {
        WeatherInfo info = new WeatherInfo();
        info.setLocation(location);
        info.setDate(date);
        
        // 随机生成天气状况
        String condition = weatherConditions[random.nextInt(weatherConditions.length)];
        info.setWeatherCondition(condition);
        
        // 根据天气状况生成相关数据
        switch (condition) {
            case "晴":
                info.setTemperature(20.0 + random.nextDouble() * 10); // 20-30度
                info.setRainfall(0.0);
                info.setWindSpeed(random.nextDouble() * 5); // 0-5m/s
                break;
            case "多云":
                info.setTemperature(15.0 + random.nextDouble() * 10); // 15-25度
                info.setRainfall(0.0);
                info.setWindSpeed(random.nextDouble() * 8); // 0-8m/s
                break;
            case "阴":
                info.setTemperature(10.0 + random.nextDouble() * 10); // 10-20度
                info.setRainfall(random.nextDouble() * 2); // 0-2mm
                info.setWindSpeed(random.nextDouble() * 10); // 0-10m/s
                break;
            case "小雨":
                info.setTemperature(8.0 + random.nextDouble() * 8); // 8-16度
                info.setRainfall(2.0 + random.nextDouble() * 3); // 2-5mm
                info.setWindSpeed(random.nextDouble() * 12); // 0-12m/s
                break;
            case "中雨":
                info.setTemperature(5.0 + random.nextDouble() * 5); // 5-10度
                info.setRainfall(5.0 + random.nextDouble() * 5); // 5-10mm
                info.setWindSpeed(random.nextDouble() * 15); // 0-15m/s
                break;
            case "大雨":
                info.setTemperature(0.0 + random.nextDouble() * 5); // 0-5度
                info.setRainfall(10.0 + random.nextDouble() * 10); // 10-20mm
                info.setWindSpeed(random.nextDouble() * 20); // 0-20m/s
                break;
            case "雷阵雨":
                info.setTemperature(15.0 + random.nextDouble() * 10); // 15-25度
                info.setRainfall(5.0 + random.nextDouble() * 15); // 5-20mm
                info.setWindSpeed(random.nextDouble() * 25); // 0-25m/s
                info.setWeatherAlert("有雷阵雨，请注意防雷安全。");
                break;
        }
        
        // 设置是否适合施工
        info.setSuitableForWork(isSuitableForWork(info));
        info.setWorkSuggestion(getWorkSuggestion(info));
        
        return info;
    }
} 