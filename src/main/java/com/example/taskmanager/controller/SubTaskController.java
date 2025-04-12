package com.example.taskmanager.controller;

import com.example.taskmanager.dto.SubTaskDto;
import com.example.taskmanager.service.SubTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "SubTask API", description = "서브 태스크 관련 API")
@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/subtasks")
@RequiredArgsConstructor
public class SubTaskController {

    private final SubTaskService subTaskService;

    /**
     * 특정 태스크의 모든 작업 목록을 조회합니다.
     */
    @Operation(summary = "특정 태스크의 모든 서브 작업 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서브 작업 목록 반환",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubTaskDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<SubTaskDto>> getSubTaskByTaskId(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId
    ) {
        List<SubTaskDto> jobs = subTaskService.getSubTaskList(taskId);
        return ResponseEntity.ok(jobs);
    }

    /**
     * 새로운 작업을 생성하거나 기존 작업을 업데이트합니다.
     */
    @Operation(summary = "서브 작업 생성 또는 수정")
    @PostMapping
    public ResponseEntity<SubTaskDto> createOrUpdateJob(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "서브 작업 정보")
            @RequestBody
            SubTaskDto subTaskDto
    ) {
        SubTaskDto saved = subTaskService.createSubTaskWithUsers(projectId, taskId, subTaskDto);
        return ResponseEntity.ok(saved);
    }

    /**
     * 특정 ID의 작업을 조회합니다.
     */
    @Operation(summary = "특정 서브 작업 조회")
    @GetMapping("/{jobId}")
    public ResponseEntity<SubTaskDto> getSubTask(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId,
            @Parameter(description = "서브 작업 ID") @PathVariable Long subTaskId) {
        SubTaskDto job = subTaskService.getSubTask(taskId, subTaskId);
        return ResponseEntity.ok(job);
    }


    /**
     * 특정 ID의 작업을 삭제합니다.
     */
    @Operation(summary = "특정 서브 작업 삭제")
    @DeleteMapping("/{subTaskId}")
    public ResponseEntity<Void> deleteSubtask(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId,
            @Parameter(description = "서브 작업 ID") @PathVariable Long jobId) {
        subTaskService.deleteSubTask(projectId, taskId, jobId);
        return ResponseEntity.noContent().build();
    }
}
