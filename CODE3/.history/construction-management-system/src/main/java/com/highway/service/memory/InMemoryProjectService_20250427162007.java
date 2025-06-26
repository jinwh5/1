package com.highway.service.memory;

import com.highway.model.Project;
import com.highway.service.interfaces.ProjectService;
import com.highway.util.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 项目服务的内存实现
 */
@Service
public class InMemoryProjectService implements ProjectService {
    
    private final ConcurrentHashMap<Long, Project> projects = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private static final String DATA_FILE = "data/projects.json";
    
    @PostConstruct
    public void init() {
        loadFromFile();
    }
    
    @Override
    public Project save(Project project) {
        if (project.getId() == null) {
            project.setId(idGenerator.getAndIncrement());
        }
        projects.put(project.getId(), project);
        saveToFile();
        return project;
    }
    
    @Override
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }
    
    @Override
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects.values());
    }
    
    @Override
    public Page<Project> getAllProjects(Pageable pageable) {
        List<Project> allProjects = getAllProjects();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allProjects.size());
        List<Project> pageContent = allProjects.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allProjects.size());
    }
    
    @Override
    public List<Project> findByName(String name) {
        return projects.values().stream()
                .filter(project -> project.getName().contains(name))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Project> findByStatus(String status) {
        return projects.values().stream()
                .filter(project -> project.getStatus().equals(status))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Project> findByLocation(String location) {
        return projects.values().stream()
                .filter(project -> project.getLocation().contains(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        projects.remove(id);
        saveToFile();
    }
    
    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        ids.forEach(projects::remove);
        saveToFile();
    }
    
    private void saveToFile() {
        JsonUtils.writeToFile(new ArrayList<>(projects.values()), DATA_FILE);
    }
    
    private void loadFromFile() {
        List<Project> loadedProjects = JsonUtils.readFromFile(DATA_FILE, Project.class);
        if (loadedProjects != null) {
            loadedProjects.forEach(project -> {
                projects.put(project.getId(), project);
                if (project.getId() >= idGenerator.get()) {
                    idGenerator.set(project.getId() + 1);
                }
            });
        }
    }
} 