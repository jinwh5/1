package com.highway.config;

import com.highway.model.Worker;
import com.highway.model.Schedule;
import com.highway.model.Attendance;
import com.highway.service.interfaces.WorkerService;
import com.highway.service.interfaces.ScheduleService;
import com.highway.service.interfaces.AttendanceService;
import com.highway.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用启动时的数据初始化配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    private static final String DATA_DIR = "data";
    private static final String WORKERS_FILE = "data/workers.json";
    private static final String SCHEDULES_FILE = "data/schedules.json";
    private static final String ATTENDANCE_FILE = "data/attendance.json";
    
    /**
     * 创建示例数据的CommandLineRunner
     * 仅在开发环境中启用
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner initData(
            WorkerService workerService,
            ScheduleService scheduleService,
            AttendanceService attendanceService) {
        return args -> {
            // 确保数据目录存在
            createDataDirectory();
            
            // 检查是否已有数据
            if (workerService.getAllWorkers().isEmpty()) {
                log.info("初始化示例工人数据");
                
                // 创建示例工人数据
                Worker worker1 = new Worker();
                worker1.setName("张三");
                worker1.setGender("男");
                worker1.setAge(32);
                worker1.setIdCard("410123198912123456");
                worker1.setPhone("13800138001");
                worker1.setPosition("施工员");
                worker1.setHireDate(LocalDate.of(2020, 5, 15));
                worker1.setStatus("在职");
                worker1.setAddress("河南省郑州市金水区");
                worker1.setRemarks("经验丰富");
                workerService.saveWorker(worker1);
                
                Worker worker2 = new Worker();
                worker2.setName("李四");
                worker2.setGender("男");
                worker2.setAge(28);
                worker2.setIdCard("410123199203123457");
                worker2.setPhone("13900139002");
                worker2.setPosition("机械操作员");
                worker2.setHireDate(LocalDate.of(2021, 3, 10));
                worker2.setStatus("在职");
                worker2.setAddress("河南省郑州市二七区");
                worker2.setRemarks("技术熟练");
                workerService.saveWorker(worker2);
                
                Worker worker3 = new Worker();
                worker3.setName("王五");
                worker3.setGender("男");
                worker3.setAge(45);
                worker3.setIdCard("410123197606123458");
                worker3.setPhone("13700137003");
                worker3.setPosition("项目经理");
                worker3.setHireDate(LocalDate.of(2018, 1, 5));
                worker3.setStatus("在职");
                worker3.setAddress("河南省郑州市管城区");
                worker3.setRemarks("管理能力强");
                workerService.saveWorker(worker3);
                
                Worker worker4 = new Worker();
                worker4.setName("赵六");
                worker4.setGender("男");
                worker4.setAge(33);
                worker4.setIdCard("410123198806123459");
                worker4.setPhone("13600136004");
                worker4.setPosition("电工");
                worker4.setHireDate(LocalDate.of(2019, 7, 12));
                worker4.setStatus("在职");
                worker4.setAddress("河南省郑州市惠济区");
                worker4.setRemarks("持有电工证");
                workerService.saveWorker(worker4);
                
                Worker worker5 = new Worker();
                worker5.setName("周七");
                worker5.setGender("女");
                worker5.setAge(29);
                worker5.setIdCard("410123199208123460");
                worker5.setPhone("13500135005");
                worker5.setPosition("安全员");
                worker5.setHireDate(LocalDate.of(2020, 9, 20));
                worker5.setStatus("在职");
                worker5.setAddress("河南省郑州市中原区");
                worker5.setRemarks("安全意识强");
                workerService.saveWorker(worker5);
                
                log.info("示例工人数据初始化完成");
                
                // 创建示例排班数据
                log.info("初始化示例排班数据");
                
                // 为工人1创建排班
                Schedule schedule1 = new Schedule();
                schedule1.setWorkerId(worker1.getId());
                schedule1.setProjectId(1L);
                schedule1.setDate(LocalDate.now());
                schedule1.setShiftType("早班");
                schedule1.setStartTime(LocalTime.of(8, 0));
                schedule1.setEndTime(LocalTime.of(16, 0));
                schedule1.setLocation("郑州市金水区花园路");
                schedule1.setStatus("已确认");
                scheduleService.createSchedule(schedule1);
                
                // 为工人2创建排班
                Schedule schedule2 = new Schedule();
                schedule2.setWorkerId(worker2.getId());
                schedule2.setProjectId(1L);
                schedule2.setDate(LocalDate.now());
                schedule2.setShiftType("中班");
                schedule2.setStartTime(LocalTime.of(16, 0));
                schedule2.setEndTime(LocalTime.of(24, 0));
                schedule2.setLocation("郑州市金水区花园路");
                schedule2.setStatus("已确认");
                scheduleService.createSchedule(schedule2);
                
                // 为工人3创建排班
                Schedule schedule3 = new Schedule();
                schedule3.setWorkerId(worker3.getId());
                schedule3.setProjectId(1L);
                schedule3.setDate(LocalDate.now());
                schedule3.setShiftType("早班");
                schedule3.setStartTime(LocalTime.of(8, 0));
                schedule3.setEndTime(LocalTime.of(16, 0));
                schedule3.setLocation("郑州市二七区大学路");
                schedule3.setStatus("已确认");
                scheduleService.createSchedule(schedule3);
                
                log.info("示例排班数据初始化完成");
                
                // 创建示例考勤数据
                log.info("初始化示例考勤数据");
                
                // 为工人1创建考勤记录
                Attendance attendance1 = new Attendance();
                attendance1.setWorkerId(worker1.getId());
                attendance1.setDate(LocalDate.now());
                attendance1.setCheckInTime(LocalDateTime.now().withHour(7).withMinute(55));
                attendance1.setCheckOutTime(LocalDateTime.now().withHour(16).withMinute(5));
                attendance1.setStatus("正常");
                attendance1.setWorkHours(8.0);
                attendance1.setOvertimeHours(0.0);
                attendance1.setRemarks("按时上下班");
                attendanceService.saveAttendance(attendance1);
                
                // 为工人2创建考勤记录
                Attendance attendance2 = new Attendance();
                attendance2.setWorkerId(worker2.getId());
                attendance2.setDate(LocalDate.now());
                attendance2.setCheckInTime(LocalDateTime.now().withHour(15).withMinute(50));
                attendance2.setCheckOutTime(LocalDateTime.now().withHour(23).withMinute(55));
                attendance2.setStatus("正常");
                attendance2.setWorkHours(8.0);
                attendance2.setOvertimeHours(0.0);
                attendance2.setRemarks("按时上下班");
                attendanceService.saveAttendance(attendance2);
                
                // 为工人3创建考勤记录
                Attendance attendance3 = new Attendance();
                attendance3.setWorkerId(worker3.getId());
                attendance3.setDate(LocalDate.now());
                attendance3.setCheckInTime(LocalDateTime.now().withHour(8).withMinute(10));
                attendance3.setCheckOutTime(LocalDateTime.now().withHour(16).withMinute(0));
                attendance3.setStatus("迟到");
                attendance3.setWorkHours(7.5);
                attendance3.setOvertimeHours(0.0);
                attendance3.setRemarks("迟到10分钟");
                attendanceService.saveAttendance(attendance3);
                
                log.info("示例考勤数据初始化完成");
                
                // 初始化空的数据文件
                initializeEmptyDataFiles();
            } else {
                log.info("数据已存在，跳过初始化");
            }
        };
    }
    
    /**
     * 创建数据目录
     */
    private void createDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (created) {
                log.info("创建数据目录: {}", DATA_DIR);
            } else {
                log.warn("无法创建数据目录: {}", DATA_DIR);
            }
        }
    }
    
    /**
     * 初始化空的数据文件
     */
    private void initializeEmptyDataFiles() {
        try {
            // 初始化空的排班数据文件
            JsonUtils.writeToFile(new ArrayList<Schedule>(), SCHEDULES_FILE);
            log.info("初始化排班数据文件: {}", SCHEDULES_FILE);
            
            // 初始化空的考勤数据文件
            JsonUtils.writeToFile(new ArrayList<Attendance>(), ATTENDANCE_FILE);
            log.info("初始化考勤数据文件: {}", ATTENDANCE_FILE);
        } catch (IOException e) {
            log.error("初始化数据文件失败", e);
        }
    }
} 