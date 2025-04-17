package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.*;
import com.example.taskmanager.dto.StatusSummaryDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskRequestDto;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskAssignedUserRepository taskAssignedUserRepository;
    private final ProjectRepository projectRepository;
    private final SubTaskRepository subTaskRepository;


    @Transactional
    public TaskDto createTaskWithUsers(Long projectId, TaskRequestDto taskDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        Task task = taskDto.toEntity(project);

        List<TaskAssignedUser> assignedUsers = new ArrayList<>();
        for (Long userId: taskDto.getAssignees()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
            task.addAssignedUser(new TaskAssignedUser(user, task)); // 양방향 연관관계 설정
        }

        Task savedTask = taskRepository.save(task);
        return TaskDto.from(savedTask);
    }


    public StatusSummaryDto getTaskSummary() {
        long totalCount = taskRepository.count();
        long activeCount = taskRepository.countByStatus(JobStatus.IN_PROGRESS);
        long finishedCount = taskRepository.countByStatus(JobStatus.FINISHED);
        long stoppedCount = taskRepository.countByStatus(JobStatus.STOP);
        long cancelCount = taskRepository.countByStatus(JobStatus.CANCEL);
        long repoenCount = taskRepository.countByStatus(JobStatus.REOPEN);
        long createCount = taskRepository.countByStatus(JobStatus.CREATED);
        long overDateCount = taskRepository.countByAllOverDateTask();

        StatusSummaryDto dto = new StatusSummaryDto(totalCount, activeCount,
                finishedCount, stoppedCount, cancelCount,createCount, repoenCount, overDateCount);

        return dto;
    }

    public StatusSummaryDto getSubTaskSummary(Long projectId, Long taskId) {
        long totalCount = subTaskRepository.count(projectId, taskId);
        long activeCount = subTaskRepository.countByProjectIdAndTaskIdAndStatus(projectId, taskId, JobStatus.IN_PROGRESS);
        long finishedCount = subTaskRepository.countByProjectIdAndTaskIdAndStatus(projectId, taskId, JobStatus.FINISHED);
        long stoppedCount = subTaskRepository.countByProjectIdAndTaskIdAndStatus(projectId, taskId, JobStatus.STOP);
        long cancelCount = subTaskRepository.countByProjectIdAndTaskIdAndStatus(projectId, taskId, JobStatus.CANCEL);
        long repoenCount = subTaskRepository.countByProjectIdAndTaskIdAndStatus(projectId, taskId, JobStatus.REOPEN);
        long createCount = subTaskRepository.countByProjectIdAndTaskIdAndStatus(projectId, taskId, JobStatus.CREATED);
        long overDateCount = subTaskRepository.countByOverDateTask(projectId, taskId);

        StatusSummaryDto dto = new StatusSummaryDto(totalCount, activeCount,
                finishedCount, stoppedCount, cancelCount,createCount, repoenCount, overDateCount);

        return dto;
    }


    @Transactional(readOnly = true)
    public List<TaskDto> getTaskList(Pageable pageable) {
        List<Task> tasks = taskRepository.findAll(pageable).getContent();
        return tasks.stream().map(TaskDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getTaskList(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream().map(TaskDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDto getTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));
        return TaskDto.from(task);
    }

    @Transactional
    public TaskDto updateTaskWithUsers(Long projectId, Long taskId, TaskRequestDto taskDto) {
        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));

        // 기존 연관관계 제거: 양방향 관계를 정확히 끊어줘야 Hibernate 오류 방지 가능
        List<TaskAssignedUser> existingUsers = new ArrayList<>(task.getAssignedUsers());
        task.getAssignedUsers().clear(); // 컬렉션에서 제거
        taskAssignedUserRepository.deleteAllInBatch(existingUsers); // 강제 삭제

        for (Long userId: taskDto.getAssignees()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
            TaskAssignedUser assignedUser = new TaskAssignedUser(user, task);
            task.addAssignedUser(assignedUser); // 양방향 연관관계 설정
        }

        task.update(taskDto.getName(), taskDto.getStartDate(),
                taskDto.getPlannedEndDate(), taskDto.getCompletionDate(),
                taskDto.getDescription(), taskDto.getStatus());

        return TaskDto.from(taskRepository.save(task));
    }

    @Transactional
    public TaskDto deleteTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));
        taskRepository.delete(task);
        return TaskDto.from(task);
    }
}
