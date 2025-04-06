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
    private final TaskService taskService;

    /**
     * QueryDSL을 사용하여 프로젝트를 동적으로 검색합니다.
     * 
     * @param active 활성 상태 필터링
     * @param manager 담당자 필터링
     * @param hasTasks 태스크 존재 여부 필터링
     * @param upcoming 예정된 프로젝트 필터링
     * @return 필터링된 ProjectDto 목록
     */
    @Transactional(readOnly = true)
    public List<ProjectDto> searchProjects(Boolean active, String manager, Boolean hasTasks, Boolean upcoming) {
        return projectRepository.searchProjects(active, manager, hasTasks, upcoming).stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }

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
//    @Transactional(readOnly = true)
//    public ProjectDto getProjectWithTasksById(Long id) {
//        Project project = projectRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
//
//        // TaskService를 통해 태스크 조회
//        ProjectDto projectDto = ProjectDto.from(project);
//        List<TaskDto> tasks = taskService.getTasksByProjectId(id);
//        projectDto.setTasks(tasks);
//
//        return projectDto;
//    }
    
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


    // 담당자별 프로젝트 조회
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByManager(String manager) {
        return projectRepository.findByManager(manager).stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }


}