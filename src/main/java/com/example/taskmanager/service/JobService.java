package com.example.taskmanager.service;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.repository.JobRepository;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public JobDto saveJob(Long taskId, JobDto jobDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        Job job = jobDto.toEntity(task);
        Job saved = jobRepository.save(job);
        return JobDto.from(saved);
    }

    public JobDto getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));
        return JobDto.from(job);
    }

    @Transactional
    public void removeJob(Long jobId) {
        jobRepository.deleteById(jobId);
    }
}
