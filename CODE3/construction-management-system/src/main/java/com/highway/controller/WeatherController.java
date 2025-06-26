package com.highway.controller;

import com.highway.model.WeatherInfo;
import com.highway.service.interfaces.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherInfo> getWeatherInfo(
            @RequestParam String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(weatherService.getWeatherInfo(location, date));
    }

    @PostMapping("/update")
    public ResponseEntity<WeatherInfo> updateWeatherInfo(@RequestParam String location) {
        return ResponseEntity.ok(weatherService.updateWeatherInfo(location));
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkWeatherSuitability(@RequestBody WeatherInfo weatherInfo) {
        return ResponseEntity.ok(weatherService.isSuitableForWork(weatherInfo));
    }

    @PostMapping("/suggestion")
    public ResponseEntity<String> getWorkSuggestion(@RequestBody WeatherInfo weatherInfo) {
        return ResponseEntity.ok(weatherService.getWorkSuggestion(weatherInfo));
    }
} 