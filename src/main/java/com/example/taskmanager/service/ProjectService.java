package com.example.taskmanager.service;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    
    // 모든 프로젝트 조회
    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }
    
    // 활성화된 프로젝트만 조회
    @Transactional(readOnly = true)
    public List<ProjectDto> getActiveProjects() {
        return projectRepository.findByActiveTrue().stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }
    
    // ID로 프로젝트 조회 (태스크 포함)
    @Transactional(readOnly = true)
    public ProjectDto getProjectWithTasksById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
        return ProjectDto.fromWithTasks(project);
    }
    
    // ID로 프로젝트 조회 (태스크 미포함)
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
        return ProjectDto.from(project);
    }
    
    // 프로젝트 생성
    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = projectDto.toEntity();
        Project savedProject = projectRepository.save(project);
        return ProjectDto.from(savedProject);
    }
    
    // 프로젝트 업데이트
    @Transactional
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
        
        project.update(
            projectDto.getName(),
            projectDto.getDescription(),
            projectDto.getStartDate(),
            projectDto.getEndDate(),
            projectDto.getManager()
        );
        
        Project updatedProject = projectRepository.save(project);
        return ProjectDto.from(updatedProject);
    }
    
    // 프로젝트 완료 처리
    @Transactional
    public ProjectDto completeProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
        
        project.complete();
        Project updatedProject = projectRepository.save(project);
        return ProjectDto.from(updatedProject);
    }
    
    // 프로젝트 활성화 처리
    @Transactional
    public ProjectDto activateProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
        
        project.activate();
        Project updatedProject = projectRepository.save(project);
        return ProjectDto.from(updatedProject);
    }
    
    // 프로젝트 삭제
    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    
    // 태스크를 프로젝트에 추가
    @Transactional
    public ProjectDto addTaskToProject(Long projectId, TaskDto taskDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + projectId));
        
        Task task = taskDto.toEntity();
        task.setProject(project);
        taskRepository.save(task);
        
        return ProjectDto.fromWithTasks(project);
    }
    
    // 프로젝트의 특정 태스크 조회
    @Transactional(readOnly = true)
    public TaskDto getTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + projectId));
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        if (task.getProject() == null || !task.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Task does not belong to the specified project");
        }
        
        return TaskDto.from(task);
    }
    
    // 프로젝트에서 태스크 제거
    @Transactional
    public void removeTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + projectId));
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        if (task.getProject() != null && task.getProject().getId().equals(projectId)) {
            task.setProject(null);
            taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task does not belong to the specified project");
        }
    }
    
    // 담당자별 프로젝트 조회
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByManager(String manager) {
        return projectRepository.findByManager(manager).stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }
    
    // 태스크가 있는 프로젝트 조회
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsWithTasks() {
        return projectRepository.findProjectsWithTasks().stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }
    
    // 향후 시작 예정인 프로젝트 조회
    @Transactional(readOnly = true)
    public List<ProjectDto> getUpcomingProjects() {
        return projectRepository.findUpcomingProjects().stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }
}
