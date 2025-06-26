package com.highway.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highway.model.WeatherInfo;
import com.highway.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WeatherInfo getWeatherInfo(String location, LocalDate date) {
        try {
            String url = String.format("%s?key=%s&q=%s&dt=%s", apiUrl, apiKey, location, date);
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("请求失败: " + response);
                }

                JsonNode root = objectMapper.readTree(response.body().string());
                return parseWeatherInfo(root, location, date);
            }
        } catch (Exception e) {
            log.error("获取天气信息失败", e);
            return createDefaultWeatherInfo(location, date);
        }
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

    private WeatherInfo parseWeatherInfo(JsonNode root, String location, LocalDate date) {
        WeatherInfo info = new WeatherInfo();
        info.setLocation(location);
        info.setDate(date);

        JsonNode current = root.path("current");
        info.setTemperature(current.path("temp_c").asDouble());
        info.setWeatherCondition(current.path("condition").path("text").asText());
        info.setWindSpeed(current.path("wind_kph").asDouble() / 3.6); // 转换为m/s
        info.setRainfall(current.path("precip_mm").asDouble());

        // 检查是否有天气预警
        JsonNode alerts = root.path("alerts");
        if (!alerts.isMissingNode() && alerts.size() > 0) {
            info.setWeatherAlert(alerts.path(0).path("desc").asText());
        }

        // 设置是否适合施工
        info.setSuitableForWork(isSuitableForWork(info));
        info.setWorkSuggestion(getWorkSuggestion(info));

        return info;
    }

    private WeatherInfo createDefaultWeatherInfo(String location, LocalDate date) {
        WeatherInfo info = new WeatherInfo();
        info.setLocation(location);
        info.setDate(date);
        info.setWeatherCondition("未知");
        info.setTemperature(20.0);
        info.setRainfall(0.0);
        info.setWindSpeed(0.0);
        info.setWeatherAlert("天气数据获取失败");
        info.setSuitableForWork(true);
        info.setWorkSuggestion("由于天气数据获取失败，请实地确认天气情况后决定是否施工。");
        return info;
    }
} 