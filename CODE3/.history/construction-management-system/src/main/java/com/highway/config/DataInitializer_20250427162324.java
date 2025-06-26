package com.highway.config;

import com.highway.model.Worker;
import com.highway.model.Schedule;
import com.highway.model.Attendance;
import com.highway.model.SafetyRecord;
import com.highway.model.Project;
import com.highway.service.interfaces.WorkerService;
import com.highway.service.interfaces.ScheduleService;
import com.highway.service.interfaces.AttendanceService;
import com.highway.service.interfaces.SafetyRecordService;
import com.highway.service.interfaces.ProjectService;
import com.highway.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

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
    private static final String SAFETY_RECORDS_FILE = "data/safety_records.json";
    
    @Autowired
    private SafetyRecordService safetyRecordService;
    
    @Autowired
    private ProjectService projectService;
    
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
            
            // 初始化空的数据文件
            initializeEmptyDataFiles();
            
            // 获取所有工人
            List<Worker> workers = workerService.getAllWorkers();
            
            // 如果没有工人数据，创建示例工人数据
            if (workers.isEmpty()) {
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
            } else {
                log.info("工人数据已存在，跳过初始化");
            }
            
            // 创建示例排班数据
            log.info("初始化示例排班数据");
            
            // 为每个工人创建排班
            for (Worker worker : workers) {
                // 早班
                Schedule morningShift = new Schedule();
                morningShift.setWorkerId(worker.getId());
                morningShift.setProjectId(1L);
                morningShift.setDate(LocalDate.now());
                morningShift.setShiftType("早班");
                morningShift.setStartTime(LocalTime.of(8, 0));
                morningShift.setEndTime(LocalTime.of(16, 0));
                morningShift.setLocation("郑州市金水区花园路");
                morningShift.setStatus("已确认");
                scheduleService.createSchedule(morningShift);
                
                // 中班
                Schedule afternoonShift = new Schedule();
                afternoonShift.setWorkerId(worker.getId());
                afternoonShift.setProjectId(1L);
                afternoonShift.setDate(LocalDate.now().plusDays(1));
                afternoonShift.setShiftType("中班");
                afternoonShift.setStartTime(LocalTime.of(16, 0));
                afternoonShift.setEndTime(LocalTime.of(0, 0));
                afternoonShift.setLocation("郑州市金水区花园路");
                afternoonShift.setStatus("已确认");
                scheduleService.createSchedule(afternoonShift);
            }
            
            log.info("示例排班数据初始化完成");
            
            // 创建示例考勤数据
            log.info("初始化示例考勤数据");
            
            // 为每个工人创建考勤记录
            for (Worker worker : workers) {
                // 今天的考勤
                Attendance todayAttendance = new Attendance();
                todayAttendance.setWorkerId(worker.getId());
                todayAttendance.setDate(LocalDate.now());
                todayAttendance.setCheckInTime(LocalDateTime.now().withHour(7).withMinute(55));
                todayAttendance.setCheckOutTime(LocalDateTime.now().withHour(16).withMinute(5));
                todayAttendance.setStatus("正常");
                todayAttendance.setWorkHours(8.0);
                todayAttendance.setOvertimeHours(0.0);
                todayAttendance.setRemarks("按时上下班");
                attendanceService.saveAttendance(todayAttendance);
                
                // 昨天的考勤
                Attendance yesterdayAttendance = new Attendance();
                yesterdayAttendance.setWorkerId(worker.getId());
                yesterdayAttendance.setDate(LocalDate.now().minusDays(1));
                yesterdayAttendance.setCheckInTime(LocalDateTime.now().minusDays(1).withHour(8).withMinute(10));
                yesterdayAttendance.setCheckOutTime(LocalDateTime.now().minusDays(1).withHour(16).withMinute(0));
                yesterdayAttendance.setStatus("迟到");
                yesterdayAttendance.setWorkHours(7.5);
                yesterdayAttendance.setOvertimeHours(0.0);
                yesterdayAttendance.setRemarks("迟到10分钟");
                attendanceService.saveAttendance(yesterdayAttendance);
            }
            
            log.info("示例考勤数据初始化完成");
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
            
            // 初始化空的安全监控记录数据文件
            JsonUtils.writeToFile(new ArrayList<SafetyRecord>(), SAFETY_RECORDS_FILE);
            log.info("初始化安全监控记录数据文件: {}", SAFETY_RECORDS_FILE);
        } catch (IOException e) {
            log.error("初始化数据文件失败", e);
        }
    }

    private void initializeSafetyRecords() {
        if (safetyRecordService.findAll().isEmpty()) {
            // 创建一些安全记录
            SafetyRecord record1 = new SafetyRecord();
            record1.setWorkerId(1L);
            record1.setProjectId(1L);
            record1.setEventType("设备故障");
            record1.setDescription("挖掘机液压系统出现异常");
            record1.setSeverityLevel("轻微");
            record1.setStatus("已处理");
            record1.setOccurrenceTime(LocalDateTime.now().minusDays(2));
            record1.setLocation("A区施工现场");
            record1.setMeasures("更换液压油，检修液压系统");
            record1.setRemarks("设备已恢复正常运行");
            safetyRecordService.save(record1);

            SafetyRecord record2 = new SafetyRecord();
            record2.setWorkerId(2L);
            record2.setProjectId(1L);
            record2.setEventType("人员受伤");
            record2.setDescription("工人在搬运材料时扭伤腰部");
            record2.setSeverityLevel("一般");
            record2.setStatus("处理中");
            record2.setOccurrenceTime(LocalDateTime.now().minusDays(1));
            record2.setLocation("B区材料堆放区");
            record2.setMeasures("送医治疗，加强安全培训");
            record2.setRemarks("工人正在康复中");
            safetyRecordService.save(record2);

            SafetyRecord record3 = new SafetyRecord();
            record3.setWorkerId(3L);
            record3.setProjectId(2L);
            record3.setEventType("设备事故");
            record3.setDescription("起重机吊臂发生倾斜");
            record3.setSeverityLevel("重大");
            record3.setStatus("已处理");
            record3.setOccurrenceTime(LocalDateTime.now().minusHours(12));
            record3.setLocation("C区起重作业区");
            record3.setMeasures("立即停止作业，检修设备，加强检查");
            record3.setRemarks("已对相关人员进行安全培训");
            safetyRecordService.save(record3);
        }
    }

    private void initializeProjects() {
        if (projectService.getAllProjects().isEmpty()) {
            // 创建示例项目数据
            Project project1 = new Project();
            project1.setName("郑州-开封高速公路扩建工程");
            project1.setDescription("郑州至开封段高速公路扩建工程，全长约50公里");
            project1.setLocation("河南省郑州市金水区");
            project1.setStartDate(LocalDate.of(2024, 1, 1));
            project1.setEndDate(LocalDate.of(2025, 12, 31));
            project1.setStatus("进行中");
            project1.setManager("王五");
            project1.setBudget(50000000.0);
            project1.setProgress(35);
            projectService.saveProject(project1);

            Project project2 = new Project();
            project2.setName("郑州地铁6号线建设工程");
            project2.setDescription("郑州地铁6号线一期工程，全长约30公里");
            project2.setLocation("河南省郑州市中原区");
            project2.setStartDate(LocalDate.of(2024, 3, 1));
            project2.setEndDate(LocalDate.of(2026, 12, 31));
            project2.setStatus("进行中");
            project2.setManager("李四");
            project2.setBudget(30000000.0);
            project2.setProgress(15);
            projectService.saveProject(project2);
        }
    }

    private void initializeWorkers() {
        List<Worker> workers = workerService.getAllWorkers();
        if (workers.isEmpty()) {
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
        } else {
            log.info("工人数据已存在，跳过初始化");
        }
    }

    @PostConstruct
    public void init() {
        createDataDirectory();
        initializeEmptyDataFiles();
        initializeWorkers();
        initializeProjects();
        initializeSafetyRecords();
    }
} 