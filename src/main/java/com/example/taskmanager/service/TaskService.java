package com.example.taskmanager.service;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        Task entity = dto.toEntity();
        Task saved = taskRepository.save(entity);
        return TaskDto.from(saved);
    }

    @Transactional
    public void removeTask(Long id) {
        taskRepository.deleteById(id);
    }
}
