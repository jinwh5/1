package com.highway.config;

import com.highway.model.Worker;
import com.highway.service.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

/**
 * 应用启动时的数据初始化配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    /**
     * 创建示例数据的CommandLineRunner
     * 仅在开发环境中启用
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner initData(WorkerService workerService) {
        return args -> {
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
            } else {
                log.info("工人数据已存在，跳过初始化");
            }
        };
    }
} 