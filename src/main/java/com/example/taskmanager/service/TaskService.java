package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.domain.TaskAssignedUser;
import com.example.taskmanager.dto.StatusSummaryDto;
import com.example.taskmanager.dto.TaskDto;
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
    public TaskDto createTaskWithUsers(Long projectId, TaskDto taskDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        Task task = taskDto.toEntity(project);

        List<TaskAssignedUser> assignedUsers = new ArrayList<>();
        for (var userDto : taskDto.getAssignees()) {
            User user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userDto.getId()));
            assignedUsers.add(new TaskAssignedUser(user, task));
        }

//        task.setAssignedUsers(assignedUsers);

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
    public TaskDto updateTaskWithUsers(Long projectId, Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));

        task.update(taskDto.getName(), taskDto.getStartDate(),
                taskDto.getPlannedEndDate(), taskDto.getCompletionDate(),
                taskDto.getDescription(), taskDto.getStatus());

        task.getAssignedUsers().forEach(TaskAssignedUser::removeTask);

        List<TaskAssignedUser> updatedUsers = new ArrayList<>();
        for (var userDto : taskDto.getAssignees()) {
            User user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userDto.getId()));
            updatedUsers.add(new TaskAssignedUser(user, task));
        }
//        task.getAssignedUsers().addAll(updatedUsers);

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
