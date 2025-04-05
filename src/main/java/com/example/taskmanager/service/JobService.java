package com.example.taskmanager.service;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.repository.JobRepository;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public JobDto saveJob(Long taskId, JobDto jobDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
        
        if (jobDto.getId() == null) {
            // 새로운 Job 생성
            Job job = jobDto.toEntity(task);
            Job saved = jobRepository.save(job);
            return JobDto.from(saved);
        } else {
            // 기존 Job 업데이트
            Job existingJob = jobRepository.findById(jobDto.getId())
                    .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobDto.getId()));
            
            existingJob.update(
                jobDto.getName(),
                jobDto.getAssignedUser(),
                jobDto.getDescription(),
                jobDto.getStartTime(),
                jobDto.getEndTime()
            );
            
            Job saved = jobRepository.save(existingJob);
            return JobDto.from(saved);
        }
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
    
    public List<JobDto> getJobsByTaskId(Long taskId) {
        return jobRepository.findByTaskId(taskId).stream()
                .map(JobDto::from)
                .collect(Collectors.toList());
    }
    
    public List<JobDto> getJobsByTaskIdAndCompleted(Long taskId, boolean completed) {
        return jobRepository.findByTaskIdAndCompleted(taskId, completed).stream()
                .map(JobDto::from)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public JobDto completeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));
        
        job.complete();
        Job savedJob = jobRepository.save(job);
        return JobDto.from(savedJob);
    }
    
    public double getTaskCompletionPercentage(Long taskId) {
        long completedJobs = jobRepository.countCompletedJobsByTaskId(taskId);
        long totalJobs = jobRepository.countJobsByTaskId(taskId);
        
        if (totalJobs == 0) {
            return 0.0;
        }
        
        return (double) completedJobs / totalJobs * 100;
    }
}
