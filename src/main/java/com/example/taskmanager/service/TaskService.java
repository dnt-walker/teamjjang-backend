package com.example.taskmanager.service;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.TaskDto;
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

    public List<TaskDto> getAllTaskDtos() {
        return taskRepository.findAll().stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskDtoById(Long id) {
        return taskRepository.findById(id)
                .map(TaskDto::from)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    @Transactional
    public TaskDto saveTask(TaskDto dto) {
        if (dto.getId() == null) {
            // 새로운 Task 생성
            Task entity = dto.toEntity();
            Task saved = taskRepository.save(entity);
            return TaskDto.from(saved);
        } else {
            // 기존 Task 업데이트
            Task existingTask = taskRepository.findById(dto.getId())
                    .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + dto.getId()));
            
            existingTask.update(
                dto.getName(), 
                dto.getDescription(), 
                dto.getStartDate(), 
                dto.getPlannedEndDate(), 
                dto.getAssignees()
            );
            
            Task saved = taskRepository.save(existingTask);
            return TaskDto.from(saved);
        }
    }

    @Transactional
    public void removeTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    public List<TaskDto> getTasksByCreator(String creator) {
        return taskRepository.findByCreator(creator).stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    public List<TaskDto> getTasksByAssignee(String assignee) {
        return taskRepository.findByAssigneesContaining(assignee).stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    public List<TaskDto> getOverdueTasks() {
        LocalDate currentDate = LocalDate.now();
        return taskRepository.findOverdueTasks(currentDate).stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    public List<TaskDto> getIncompleteTasks() {
        return taskRepository.findIncompleteTasks().stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    public List<TaskDto> getCompletedTasks() {
        return taskRepository.findCompletedTasks().stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    public List<TaskDto> getActiveTasksOnDate(LocalDate date) {
        return taskRepository.findActiveTasksOnDate(date).stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TaskDto completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
        
        task.complete();
        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }
    
    @Transactional
    public TaskDto addAssignee(Long taskId, String assignee) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        task.addAssignee(assignee);
        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }
    
    @Transactional
    public TaskDto removeAssignee(Long taskId, String assignee) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        task.removeAssignee(assignee);
        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }
    
    public List<TaskDto> searchTasks(String keyword, String creator, String assignee, 
                                   Boolean isCompleted, LocalDate startDateFrom, 
                                   LocalDate startDateTo, LocalDate endDateFrom, 
                                   LocalDate endDateTo) {
        return taskRepository.searchTasks(
                keyword, creator, assignee, isCompleted, startDateFrom, 
                startDateTo, endDateFrom, endDateTo
            ).stream()
            .map(TaskDto::from)
            .collect(Collectors.toList());
    }
}
