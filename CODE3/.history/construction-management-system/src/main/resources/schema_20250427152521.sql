-- 天气信息表
CREATE TABLE IF NOT EXISTS weather_info (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    location VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    weather_condition VARCHAR(50),
    temperature DOUBLE,
    rainfall DOUBLE,
    wind_speed DOUBLE,
    weather_alert TEXT,
    suitable_for_work BOOLEAN,
    work_suggestion TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 排班表
CREATE TABLE IF NOT EXISTS schedules (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    worker_id INTEGER NOT NULL,
    project_id INTEGER NOT NULL,
    date DATE NOT NULL,
    shift_type VARCHAR(20),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(100) NOT NULL,
    status VARCHAR(20),
    weather_info_id INTEGER,
    weather_impact TEXT,
    has_conflict BOOLEAN DEFAULT FALSE,
    conflict_description TEXT,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (weather_info_id) REFERENCES weather_info(id)
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_weather_info_location_date ON weather_info(location, date);
CREATE INDEX IF NOT EXISTS idx_schedules_worker_date ON schedules(worker_id, date);
CREATE INDEX IF NOT EXISTS idx_schedules_project_date ON schedules(project_id, date);
CREATE INDEX IF NOT EXISTS idx_schedules_location_date ON schedules(location, date); 