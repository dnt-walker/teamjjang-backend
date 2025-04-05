package com.example.taskmanager.service;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.repository.JobRepository;
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

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTaskDtos() {
        return taskRepository.findAll().stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDto getTaskDtoById(Long id) {
        return taskRepository.findById(id)
                .map(TaskDto::from)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    @Transactional
    public TaskDto saveTask(TaskDto dto) {
        Task entity = dto.toEntity();
        Task saved = taskRepository.save(entity);
        return TaskDto.from(saved);
    }
    
    @Transactional
    public TaskDto updateTask(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
        
        task.update(
            dto.getName(), 
            dto.getDescription(), 
            dto.getStartDate(), 
            dto.getPlannedEndDate(), 
            dto.getAssignees()
        );
        
        Task updated = taskRepository.save(task);
        return TaskDto.from(updated);
    }

    @Transactional
    public void removeTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByCreator(String creator) {
        return taskRepository.findByCreator(creator).stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByAssignee(String assignee) {
        return taskRepository.findByAssigneesContaining(assignee).stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TaskDto> getCompletedTasks() {
        return taskRepository.findByCompletionDateIsNotNull().stream()
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
    public TaskDto reopenTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
        
        task.reopen();
        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }
    
    @Transactional
    public TaskDto addAssigneeToTask(Long taskId, String assignee) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        task.addAssignee(assignee);
        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }
    
    @Transactional
    public TaskDto removeAssigneeFromTask(Long taskId, String assignee) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        task.removeAssignee(assignee);
        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }
    
    @Transactional
    public JobDto addJobToTask(Long taskId, JobDto jobDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        Job job = jobDto.toEntity(task);
        Job saved = jobRepository.save(job);
        task.addJob(saved);
        taskRepository.save(task);
        
        return JobDto.from(saved);
    }
    
    @Transactional
    public JobDto getJobFromTask(Long taskId, Long jobId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));
        
        if (job.getTask() == null || !job.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("Job does not belong to the specified task");
        }
        
        return JobDto.from(job);
    }
    
    @Transactional
    public JobDto updateJobInTask(Long taskId, Long jobId, JobDto jobDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));
        
        if (job.getTask() == null || !job.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("Job does not belong to the specified task");
        }
        
        job.update(
            jobDto.getName(),
            jobDto.getAssignedUser(),
            jobDto.getDescription(),
            jobDto.getStartTime(),
            jobDto.getEndTime()
        );
        
        Job updated = jobRepository.save(job);
        return JobDto.from(updated);
    }
    
    @Transactional
    public void removeJobFromTask(Long taskId, Long jobId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));
        
        if (job.getTask() == null || !job.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("Job does not belong to the specified task");
        }
        
        task.removeJob(job);
        jobRepository.delete(job);
    }
}
