package com.example.taskmanager.controller;

import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/jobs")
@RequiredArgsConstructor
@Tag(name = "작업", description = "작업 관리 API")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "태스크별 작업 목록 조회", description = "특정 태스크의 모든 작업을 조회합니다")
    @ApiResponse(responseCode = "200", description = "작업 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)))
    public ResponseEntity<List<JobDto>> getJobsByTaskId(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long taskId) {
        List<JobDto> jobs = jobService.getJobsByTaskId(taskId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/status")
    @Operation(summary = "완료 상태별 작업 조회", description = "특정 태스크의 작업을 완료 상태로 필터링하여 조회합니다")
    @ApiResponse(responseCode = "200", description = "필터링된 작업 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)))
    public ResponseEntity<List<JobDto>> getJobsByStatus(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long taskId,
            @Parameter(description = "완료 상태 (true/false)", required = true)
            @RequestParam boolean completed) {
        List<JobDto> jobs = jobService.getJobsByTaskIdAndCompleted(taskId, completed);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    @Operation(summary = "작업 생성 또는 수정", description = "특정 태스크에 새 작업을 생성하거나 기존 작업을 수정합니다")
    @ApiResponse(responseCode = "200", description = "작업 생성/수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)))
    @ApiResponse(responseCode = "404", description = "태스크를 찾을 수 없음")
    public ResponseEntity<JobDto> createOrUpdateJob(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long taskId,
            @Parameter(description = "생성 또는 수정할 작업 정보", required = true)
            @RequestBody JobDto jobDto
    ) {
        JobDto saved = jobService.saveJob(taskId, jobDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "작업 상세 조회", description = "작업 ID로 특정 작업을 조회합니다")
    @ApiResponse(responseCode = "200", description = "작업 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)))
    @ApiResponse(responseCode = "404", description = "작업을 찾을 수 없음")
    public ResponseEntity<JobDto> getJob(
            @Parameter(description = "작업 ID", required = true)
            @PathVariable Long jobId) {
        JobDto job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{jobId}")
    @Operation(summary = "작업 삭제", description = "작업 ID로 작업을 삭제합니다")
    @ApiResponse(responseCode = "204", description = "작업 삭제 성공")
    @ApiResponse(responseCode = "404", description = "작업을 찾을 수 없음")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "작업 ID", required = true)
            @PathVariable Long jobId) {
        jobService.removeJob(jobId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{jobId}/complete")
    @Operation(summary = "작업 완료 처리", description = "작업을 완료 상태로 변경하고 완료 시간을 설정합니다")
    @ApiResponse(responseCode = "200", description = "작업 완료 처리 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)))
    @ApiResponse(responseCode = "404", description = "작업을 찾을 수 없음")
    public ResponseEntity<JobDto> completeJob(
            @Parameter(description = "작업 ID", required = true)
            @PathVariable Long jobId) {
        JobDto completedJob = jobService.completeJob(jobId);
        return ResponseEntity.ok(completedJob);
    }
    
    @GetMapping("/completion-percentage")
    @Operation(summary = "태스크 완료율 조회", description = "태스크의 작업 완료 비율을 백분율로 조회합니다")
    @ApiResponse(responseCode = "200", description = "완료율 조회 성공")
    public ResponseEntity<Double> getTaskCompletionPercentage(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long taskId) {
        double percentage = jobService.getTaskCompletionPercentage(taskId);
        return ResponseEntity.ok(percentage);
    }
}
