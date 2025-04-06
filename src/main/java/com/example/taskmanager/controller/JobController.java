package com.example.taskmanager.controller;

import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.dto.Status;
import com.example.taskmanager.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "작업 관리 API")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "작업 목록 조회", description = "모든 작업 목록을 조회합니다.")
    public ResponseEntity<List<JobDto>> getJobs(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "태스크 ID") Long taskId,
            @RequestParam(required = false) @Parameter(description = "작업 완료 여부 필터링 (true: 완료, false: 진행중)") Boolean completed,
            @RequestParam(required = false) @Parameter(description = "작업 담당자 필터링") String assignedUser,
            @AuthenticationPrincipal UserDetails userDetails) {
        
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
    public ResponseEntity<JobDto> getJob(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "태스크 ID") Long taskId,
            @PathVariable @Parameter(description = "작업 ID") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        JobDto job = jobService.getJobById(projectId, taskId, id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    @Operation(summary = "작업 전체 수정", description = "특정 ID의 작업 정보를 전체 수정합니다.")
    public ResponseEntity<JobDto> updateJob(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "태스크 ID") Long taskId,
            @PathVariable @Parameter(description = "수정할 작업 ID") Long id,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정된 작업 정보") JobDto jobDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        JobDto updated = jobService.updateJob(projectId, taskId, id, jobDto);
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "작업 상태 변경", description = "특정 ID의 작업 상태를 변경합니다.")
    public ResponseEntity<JobDto> updateJobStatus(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "태스크 ID") Long taskId,
            @PathVariable @Parameter(description = "상태를 변경할 작업 ID") Long id,
            @RequestParam @Parameter(description = "변경할 상태 (CREATED, WAITING, IN_PROGRESS, SUCCEEDED, FAILED, FINISHED, REMOVED)") Status status,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Status 기반으로 작업 상태 변경
        JobDto updated = jobService.changeJobStatus(projectId, taskId, id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "특정 ID의 작업을 삭제합니다.")
    public ResponseEntity<Void> deleteJob(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "태스크 ID") Long taskId,
            @PathVariable @Parameter(description = "삭제할 작업 ID") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        jobService.removeJob(projectId, taskId, id);
        return ResponseEntity.noContent().build();
    }
}
