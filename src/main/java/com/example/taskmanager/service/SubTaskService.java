package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.SubTask;
import com.example.taskmanager.domain.SubTaskAssignedUser;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.SubTaskDto;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.SubTaskRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.repository.SubTaskAssignedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SubTaskDto> getSubTaskList(Long taskId) {
        List<SubTask> subTasks = subTaskRepository.findByTaskId(taskId);
        return subTasks.stream()
                .map(SubTaskDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubTaskDto getSubTask(Long taskId, Long subTaskId) {
        SubTask subTask = subTaskRepository.findByTaskIdAndSubTaskId(taskId, subTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask", "taskId", taskId));
        return SubTaskDto.from(subTask);
    }

    @Transactional
    public SubTaskDto createSubTaskWithUsers(Long projectId, Long taskId, SubTaskDto subTaskDto) {
        Task task = taskRepository.findByProjectIdAndId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", subTaskDto.getTask().getId()));

        SubTask newSubTask = subTaskDto.toEntity(task);

        List<SubTaskAssignedUser> assignedUserList = new ArrayList<>();
        for (var assigneeDto : subTaskDto.getAssignees()) {
            User user = userRepository.findById(assigneeDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", assigneeDto.getId()));
            SubTaskAssignedUser assignedUser = new SubTaskAssignedUser(user, newSubTask);
            assignedUserList.add(assignedUser);
        }

//        newSubTask.setAssignedUsers(assignedUserList); // 연관관계 설정

        SubTask savedSubTask = subTaskRepository.save(newSubTask); // CascadeType.ALL로 assignedUsers 저장됨

        return SubTaskDto.from(savedSubTask);
    }

    @Transactional
    public SubTaskDto updateSubTaskWithUsers(Long projectId, Long taskId, SubTaskDto subTaskDto) {
        SubTask existingSubTask = subTaskRepository.findByTaskIdAndSubTaskId(taskId, subTaskDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("SubTask", "subTaskId", subTaskDto.getId()));

        existingSubTask.update(subTaskDto.getName(),
                subTaskDto.getDescription(), subTaskDto.getStartTime(),
                subTaskDto.getEndTime(), subTaskDto.getStatus());

        // 기존 사용자 제거
        existingSubTask.getAssignedUsers().forEach(SubTaskAssignedUser::removeTask);

        // 새로운 사용자 추가
        List<SubTaskAssignedUser> newAssignedUsers = new ArrayList<>();
        for (var assigneeDto : subTaskDto.getAssignees()) {
            User user = userRepository.findById(assigneeDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", assigneeDto.getId()));
            newAssignedUsers.add(new SubTaskAssignedUser(user, existingSubTask));
        }
        existingSubTask.getAssignedUsers().addAll(newAssignedUsers);

        return SubTaskDto.from(subTaskRepository.save(existingSubTask));
    }

    @Transactional
    public SubTaskDto deleteSubTask(Long projectId, Long taskId, Long subTaskId){
        SubTask subTask = subTaskRepository.findByTaskIdAndSubTaskId(taskId, subTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask", "subTaskId", subTaskId));
        subTaskRepository.delete(subTask);
        return SubTaskDto.from(subTask);
    }
}