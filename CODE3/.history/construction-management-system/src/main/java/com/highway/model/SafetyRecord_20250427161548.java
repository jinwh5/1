package com.highway.model;

import java.time.LocalDateTime;

public class SafetyRecord {
    private Long id;
    private Long workerId;
    private String workerName;
    private Long projectId;
    private String projectName;
    private String eventType;
    private String severityLevel;
    private String description;
    private String location;
    private LocalDateTime occurrenceTime;
    private String status;
    private String measures;
    private String remarks;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(LocalDateTime occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
} 