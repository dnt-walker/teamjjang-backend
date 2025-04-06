package com.example.taskmanager.service;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.dto.JobStatus;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.repository.JobRepository;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final JobRepository jobRepository;
    private final ProjectRepository projectRepository;
    
    /**
     * 프로젝트 ID로 태스크 리스트를 조회합니다.
     * JobStatus 기준으로 필터링할 수 있습니다.
     * 
     * @param projectId 프로젝트 ID
     * @param jobStatus 작업 상태 (필터링용)
     * @return 태스크 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByProjectId(Long projectId, JobStatus jobStatus) {
        List<Task> tasks;
        
        // 프로젝트에 속한 모든 태스크 조회
        if (jobStatus == null) {
            tasks = taskRepository.findByProjectId(projectId);
            return tasks.stream()
                    .map(TaskDto::fromWithProject)
                    .collect(Collectors.toList());
        }
        
        // JobStatus에 따른 필터링 처리
        switch (jobStatus) {
            case CREATED:
                // 생성된 태스크 (아직 시작되지 않은 태스크)
                tasks = taskRepository.findByProjectIdAndStartDateIsNull(projectId);
                break;
            case WAITING:
                // 대기 중인 태스크 (미래 시작일이 설정된 태스크)
                LocalDate today = LocalDate.now();
                tasks = taskRepository.findByProjectIdAndStartDateAfter(projectId, today);
                break;
            case IN_PROGRESS:
                // 진행 중인 태스크 (시작되었지만 완료되지 않은 태스크)
                tasks = taskRepository.findByProjectIdAndStartDateIsNotNullAndCompletionDateIsNull(projectId);
                break;
            case SUCCEEDED:
            case FINISHED:
                // 완료된 태스크
                tasks = taskRepository.findByProjectIdAndCompletionDateIsNotNull(projectId);
                break;
            case FAILED:
                // 실패한 태스크 (기한이 지났지만 완료되지 않은 태스크)
                LocalDate now = LocalDate.now();
                tasks = taskRepository.findByProjectIdAndPlannedEndDateBeforeAndCompletionDateIsNull(projectId, now);
                break;
            case REMOVED:
                // 논리적으로 삭제된 태스크는 표시하지 않음
                return Collections.emptyList();
            default:
                // 기본적으로 모든 태스크 반환
                tasks = taskRepository.findByProjectId(projectId);
        }
        
        return tasks.stream()
                .map(TaskDto::fromWithProject)
                .collect(Collectors.toList());
    }
    
    /**
     * 프로젝트 ID와 완료 여부로 태스크 리스트를 조회합니다.
     * 
     * @param projectId 프로젝트 ID
     * @param completed 완료 여부
     * @return 태스크 DTO 리스트
     */
    @Deprecated
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByProjectIdAndCompleted(Long projectId, Boolean completed) {
        return null;
    }
    
    /**
     * 특정 프로젝트에서 태스크를 조회합니다.
     * 
     * @param projectId 프로젝트 ID
     * @param taskId 태스크 ID
     * @return 태스크 DTO
     */
    @Transactional(readOnly = true)
    public TaskDto getTaskFromProject(Long projectId, Long taskId) {
        return taskRepository.findByIdAndProjectId(taskId, projectId)
                .map(TaskDto::fromWithProject)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId + " in project: " + projectId));
    }
    
    /**
     * 프로젝트에서 태스크를 제거합니다.
     * 
     * @param projectId 프로젝트 ID
     * @param taskId 태스크 ID
     */
    @Transactional
    public void removeTaskFromProject(Long projectId, Long taskId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId + " in project: " + projectId));
        
        // 태스크에서 프로젝트 참조 제거
        task.setProject(null);
        taskRepository.save(task);
    }
    
    /**
     * 프로젝트에 태스크를 추가합니다.
     * 
     * @param projectId 프로젝트 ID
     * @param taskDto 태스크 DTO
     * @return 생성된 태스크 DTO
     */
    @Transactional
    public TaskDto addTaskToProject(Long projectId, TaskDto taskDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + projectId));
        
        Task task = taskDto.toEntity();
        task.setProject(project);
        Task saved = taskRepository.save(task);
        
        return TaskDto.fromWithProject(saved);
    }
    
    /**
     * 프로젝트의 특정 태스크를 업데이트합니다.
     * 
     * @param projectId 프로젝트 ID
     * @param taskId 태스크 ID
     * @param taskDto 업데이트할 태스크 정보
     * @return 업데이트된 태스크 DTO
     */
    @Transactional
    public TaskDto updateProjectTask(Long projectId, Long taskId, TaskDto taskDto) {
        // 프로젝트와 태스크 가져오기
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId + " in project: " + projectId));
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + projectId));
        
        // 태스크 업데이트
        task.update(
            taskDto.getName(),
            taskDto.getDescription(),
            taskDto.getStartDate(),
            taskDto.getPlannedEndDate(),
            taskDto.getAssignees()
        );
        
        // 완료 상태 처리
        if (taskDto.getCompletionDate() != null) {
            task.setCompletionDate(taskDto.getCompletionDate());
        } else if (task.getCompletionDate() != null && taskDto.getCompletionDate() == null) {
            // 완료되었던 태스크를 미완료 상태로 변경
            task.setCompletionDate(null);
        }
        
        // 변경된 태스크 저장
        Task savedTask = taskRepository.save(task);
        
        return TaskDto.fromWithProject(savedTask);
    }
    
    /**
     * QueryDSL을 사용하여 태스크를 동적으로 검색합니다.
     * 
     * @param completed 완료 여부 필터링
     * @param assignee 담당자 필터링
     * @param creator 생성자 필터링
     * @return 필터링된 TaskDto 목록
     */
    @Transactional(readOnly = true)
    public List<TaskDto> searchTasks(Boolean completed, String assignee, String creator) {
        // QueryDSL을 사용한 동적 검색 기능
        return taskRepository.searchTasks(null, creator, assignee, completed, null, null, null, null)
                .stream()
                .map(TaskDto::fromWithProject)
                .collect(Collectors.toList());
    }
}
