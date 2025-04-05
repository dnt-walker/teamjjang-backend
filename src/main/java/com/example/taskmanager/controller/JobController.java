package com.example.taskmanager.controller;

import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "작업 관리 API")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "작업 목록 조회", description = "모든 작업 목록을 조회합니다.")
    public ResponseEntity<List<JobDto>> getJobs(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String assignedUser) {
        
        // 필터링 로직 - 서비스에 해당 메서드들 추가 필요
        if (Boolean.TRUE.equals(completed)) {
            return ResponseEntity.ok(jobService.getCompletedJobs());
        } else if (assignedUser != null && !assignedUser.isEmpty()) {
            return ResponseEntity.ok(jobService.getJobsByAssignedUser(assignedUser));
        }
        
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "작업 상세 조회", description = "특정 ID의 작업 정보를 조회합니다.")
    public ResponseEntity<JobDto> getJob(@PathVariable Long id) {
        JobDto job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    @Operation(summary = "작업 전체 수정", description = "특정 ID의 작업 정보를 전체 수정합니다.")
    public ResponseEntity<JobDto> updateJob(
            @PathVariable Long id,
            @RequestBody JobDto jobDto) {
        
        JobDto updated = jobService.updateJob(id, jobDto);
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "작업 상태 변경", description = "특정 ID의 작업 완료 상태를 변경합니다.")
    public ResponseEntity<JobDto> updateJobStatus(
            @PathVariable Long id,
            @RequestParam(required = true) boolean completed) {
        
        JobDto updated = completed
            ? jobService.completeJob(id)
            : jobService.reopenJob(id); // 재오픈 메서드 추가 필요
        
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "특정 ID의 작업을 삭제합니다.")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.removeJob(id);
        return ResponseEntity.noContent().build();
    }
}
