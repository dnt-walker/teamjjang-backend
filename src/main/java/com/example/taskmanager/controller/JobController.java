package com.example.taskmanager.controller;

import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobDto> createOrUpdateJob(
            @PathVariable Long taskId,
            @RequestBody JobDto jobDto
    ) {
        JobDto saved = jobService.saveJob(taskId, jobDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDto> getJob(@PathVariable Long jobId) {
        JobDto job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.removeJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
