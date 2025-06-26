package com.highway.service;

import com.highway.model.Schedule;
import com.highway.model.WeatherInfo;
import com.highway.repository.interfaces.ScheduleRepository;
import com.highway.service.impl.ScheduleServiceImpl;
import com.highway.service.interfaces.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Schedule testSchedule;
    private WeatherInfo testWeatherInfo;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testSchedule = new Schedule();
        testSchedule.setId(1L);
        testSchedule.setWorkerId(1L);
        testSchedule.setProjectId(1L);
        testSchedule.setDate(LocalDate.now());
        testSchedule.setShiftType("早班");
        testSchedule.setStartTime(LocalTime.of(8, 0));
        testSchedule.setEndTime(LocalTime.of(17, 0));
        testSchedule.setLocation("测试地点");
        testSchedule.setStatus("待确认");

        testWeatherInfo = new WeatherInfo();
        testWeatherInfo.setLocation("测试地点");
        testWeatherInfo.setDate(LocalDate.now());
        testWeatherInfo.setWeatherCondition("晴");
        testWeatherInfo.setTemperature(25.0);
        testWeatherInfo.setRainfall(0.0);
        testWeatherInfo.setWindSpeed(3.0);
        testWeatherInfo.setSuitableForWork(true);
    }

    @Test
    void createSchedule_Success() {
        // 准备测试数据
        when(weatherService.getWeatherInfo(anyString(), any(LocalDate.class)))
                .thenReturn(testWeatherInfo);
        when(scheduleRepository.save(any(Schedule.class)))
                .thenReturn(testSchedule);

        // 执行测试
        Schedule result = scheduleService.createSchedule(testSchedule);

        // 验证结果
        assertNotNull(result);
        assertEquals(testSchedule.getId(), result.getId());
        assertEquals(testSchedule.getWorkerId(), result.getWorkerId());
        assertEquals(testSchedule.getProjectId(), result.getProjectId());
        assertEquals(testSchedule.getDate(), result.getDate());
        assertEquals(testSchedule.getShiftType(), result.getShiftType());
        assertEquals(testSchedule.getStartTime(), result.getStartTime());
        assertEquals(testSchedule.getEndTime(), result.getEndTime());
        assertEquals(testSchedule.getLocation(), result.getLocation());
        assertEquals(testSchedule.getStatus(), result.getStatus());

        // 验证方法调用
        verify(weatherService).getWeatherInfo(anyString(), any(LocalDate.class));
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void updateSchedule_Success() {
        // 准备测试数据
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(testSchedule));
        when(weatherService.getWeatherInfo(anyString(), any(LocalDate.class)))
                .thenReturn(testWeatherInfo);
        when(scheduleRepository.save(any(Schedule.class)))
                .thenReturn(testSchedule);

        // 执行测试
        Schedule result = scheduleService.updateSchedule(testSchedule);

        // 验证结果
        assertNotNull(result);
        assertEquals(testSchedule.getId(), result.getId());

        // 验证方法调用
        verify(scheduleRepository).findById(anyLong());
        verify(weatherService).getWeatherInfo(anyString(), any(LocalDate.class));
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void updateSchedule_NotFound() {
        // 准备测试数据
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.updateSchedule(testSchedule);
        });

        // 验证方法调用
        verify(scheduleRepository).findById(anyLong());
        verify(weatherService, never()).getWeatherInfo(anyString(), any(LocalDate.class));
        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    void deleteSchedule_Success() {
        // 执行测试
        scheduleService.deleteSchedule(1L);

        // 验证方法调用
        verify(scheduleRepository).deleteById(1L);
    }

    @Test
    void getSchedule_Success() {
        // 准备测试数据
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(testSchedule));

        // 执行测试
        Schedule result = scheduleService.getSchedule(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testSchedule.getId(), result.getId());

        // 验证方法调用
        verify(scheduleRepository).findById(1L);
    }

    @Test
    void getSchedule_NotFound() {
        // 准备测试数据
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.getSchedule(1L);
        });

        // 验证方法调用
        verify(scheduleRepository).findById(1L);
    }

    @Test
    void getAllSchedules_Success() {
        // 准备测试数据
        List<Schedule> schedules = Arrays.asList(testSchedule);
        when(scheduleRepository.findAll())
                .thenReturn(schedules);

        // 执行测试
        List<Schedule> result = scheduleService.getAllSchedules();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSchedule.getId(), result.get(0).getId());

        // 验证方法调用
        verify(scheduleRepository).findAll();
    }

    @Test
    void getSchedules_Success() {
        // 准备测试数据
        Page<Schedule> page = new PageImpl<>(Arrays.asList(testSchedule));
        when(scheduleRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        // 执行测试
        Page<Schedule> result = scheduleService.getSchedules(PageRequest.of(0, 10));

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testSchedule.getId(), result.getContent().get(0).getId());

        // 验证方法调用
        verify(scheduleRepository).findAll(any(PageRequest.class));
    }

    @Test
    void checkScheduleConflict_NoConflict() {
        // 准备测试数据
        when(scheduleRepository.findByWorkerIdAndDateBetween(
                anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList());

        // 执行测试
        boolean result = scheduleService.checkScheduleConflict(testSchedule);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(scheduleRepository).findByWorkerIdAndDateBetween(
                anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void checkScheduleConflict_HasConflict() {
        // 准备测试数据
        Schedule conflictSchedule = new Schedule();
        conflictSchedule.setId(2L);
        conflictSchedule.setWorkerId(1L);
        conflictSchedule.setDate(LocalDate.now());
        conflictSchedule.setStartTime(LocalTime.of(9, 0));
        conflictSchedule.setEndTime(LocalTime.of(18, 0));

        when(scheduleRepository.findByWorkerIdAndDateBetween(
                anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(conflictSchedule));

        // 执行测试
        boolean result = scheduleService.checkScheduleConflict(testSchedule);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(scheduleRepository).findByWorkerIdAndDateBetween(
                anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void updateScheduleWeatherInfo_Success() {
        // 准备测试数据
        when(weatherService.getWeatherInfo(anyString(), any(LocalDate.class)))
                .thenReturn(testWeatherInfo);

        // 执行测试
        Schedule result = scheduleService.updateScheduleWeatherInfo(testSchedule);

        // 验证结果
        assertNotNull(result);
        assertEquals(testWeatherInfo.getWeatherCondition(), result.getWeatherCondition());
        assertEquals(testWeatherInfo.getTemperature(), result.getTemperature());
        assertEquals(testWeatherInfo.getRainfall(), result.getRainfall());
        assertEquals(testWeatherInfo.getWindSpeed(), result.getWindSpeed());
        assertEquals(testWeatherInfo.getSuitableForWork(), result.getSuitableForWork());

        // 验证方法调用
        verify(weatherService).getWeatherInfo(anyString(), any(LocalDate.class));
    }
} 