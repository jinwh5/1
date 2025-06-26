# 公路施工人员管理系统

这是一个基于Spring Boot实现的公路施工人员管理系统，使用内存存储数据（支持JSON文件持久化）。系统提供了工人信息管理、考勤记录、项目管理、排班管理和安全记录等功能。

## 功能特点

- **无数据库设计**：使用内存集合存储数据，无需配置数据库
- **JSON持久化**：自动将数据保存到JSON文件，应用重启时自动加载
- **工人管理**：添加、查询、修改、删除工人信息，支持按姓名、职位、状态筛选
- **考勤管理**：记录工人的考勤信息，包括签到、签退时间和状态
- **项目管理**：管理施工项目的基本信息和状态
- **排班管理**：安排工人排班，设置班次和工作时间
- **安全记录**：记录施工安全事件和处理状态

## 技术栈

- **后端**：Java 8, Spring Boot 2.7.3
- **前端**：Thymeleaf, Bootstrap 4, jQuery
- **数据存储**：内存集合 (ConcurrentHashMap) + JSON文件持久化
- **构建工具**：Maven

## 系统需求

- JDK 8或更高版本
- Maven 3.5或更高版本

## 快速开始

1. **克隆仓库**

```bash
git clone https://github.com/yourusername/construction-management-system.git
cd construction-management-system
```

2. **构建项目**

```bash
mvn clean package
```

3. **运行应用**

```bash
java -jar target/construction-management-system-0.0.1-SNAPSHOT.jar
```

或者使用Spring Boot Maven插件：

```bash
mvn spring-boot:run
```

4. **访问系统**

在浏览器中打开 [http://localhost:8080](http://localhost:8080)

## 系统结构

```
construction-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── highway/
│   │   │           ├── config/             # 配置类
│   │   │           ├── controller/         # 控制器
│   │   │           ├── model/              # 数据模型
│   │   │           ├── repository/         # 仓库
│   │   │           │   ├── memory/         # 内存实现
│   │   │           │   └── interfaces/     # 接口定义
│   │   │           ├── service/            # 服务
│   │   │           │   ├── interfaces/     # 服务接口
│   │   │           │   └── impl/           # 服务实现
│   │   │           ├── utils/              # 工具类
│   │   │           └── Application.java    # 应用入口
│   │   │
│   │   └── resources/
│   │       ├── static/                     # 静态资源
│   │       │   └── css/
│   │       │       └── style.css           # 自定义样式
│   │       ├── templates/                  # 页面模板
│   │       │   ├── layout/                 # 布局模板
│   │       │   ├── worker/                 # 工人相关页面
│   │       │   ├── attendance/             # 考勤相关页面
│   │       │   ├── project/                # 项目相关页面
│   │       │   ├── schedule/               # 排班相关页面
│   │       │   ├── safety/                 # 安全相关页面
│   │       │   └── index.html              # 首页
│   │       └── application.properties      # 应用配置
│   │
│   └── test/                               # 测试代码
└── pom.xml                                 # Maven配置
```

## 数据文件

系统启动后会在当前目录下自动创建`data`文件夹，用于存储JSON数据文件：

- `data/workers.json` - 工人数据
- `data/attendances.json` - 考勤数据
- `data/projects.json` - 项目数据
- `data/schedules.json` - 排班数据
- `data/safety_records.json` - 安全记录数据

## 开发模式

在`application.properties`中设置`spring.profiles.active=dev`可启用开发模式，系统将自动初始化测试数据。

## 注意事项

- 本系统使用内存存储，重启后数据会从JSON文件加载
- 适合数据量不大的应用场景
- 若要扩展为多实例部署，需考虑数据同步问题

## 许可证

[MIT License](LICENSE) 