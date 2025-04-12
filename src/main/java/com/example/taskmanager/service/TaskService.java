package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.domain.TaskAssignedUser;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.repository.TaskAssignedUserRepository;
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
    private final UserRepository userRepository;
    private final TaskAssignedUserRepository taskAssignedUserRepository;
    private final ProjectRepository projectRepository;



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
