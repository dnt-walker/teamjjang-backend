package com.example.taskmanager.controller;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.dto.StatusSummaryDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.security.TeamUserDetails;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "태스크 관리 API")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Operation(summary = "특정 프로젝트의 모든 태스크 목록 조회")
    @GetMapping("/api/tasks")
    public ResponseEntity<List<TaskDto>> getTaskList(
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        List<TaskDto> tasks = taskService.getTaskList(pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "모든 태스크 상태 리포팅")
    @GetMapping("/api/tasks-summaries")
    public ResponseEntity<StatusSummaryDto> getTaskList() {
        StatusSummaryDto summaryDto = taskService.getTaskSummary();
        return ResponseEntity.ok(summaryDto);
    }

    @Operation(summary = "특정 프로젝트의 모든 태스크 목록 조회")
    @GetMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getTaskList(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId) {
        List<TaskDto> tasks = taskService.getTaskList(projectId);
        return ResponseEntity.ok(tasks);
    }


    @Operation(summary = "태스크 생성 또는 수정")
    @PostMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> createOrUpdateTask(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "태스크 정보") @RequestBody TaskDto taskDto
    ) {
        TaskDto saved = taskService.createTaskWithUsers(projectId, taskDto);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "특정 태스크 조회")
    @GetMapping("/api/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTask(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId
    ) {
        TaskDto task = taskService.getTask(projectId, taskId);
        return ResponseEntity.ok(task);
    }



    @Operation(summary = "특정 태스크 삭제")
    @DeleteMapping("/api/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId
    ) {
        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "특정 태스크 수정")
    @PutMapping("/api/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 태스크 정보") @RequestBody TaskDto taskDto
    ) {
        TaskDto updatedTask = taskService.updateTaskWithUsers(projectId, taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }


    @Operation(summary = "특정 태스크 Sub Task 진행상태 조회")
    @GetMapping("/api/projects/{projectId}/tasks/{taskId}/summaries")
    public ResponseEntity<StatusSummaryDto> getSubTaskSummary(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @Parameter(description = "태스크 ID") @PathVariable Long taskId
    ) {
        StatusSummaryDto summaryDto = taskService.getSubTaskSummary(projectId, taskId);
        return ResponseEntity.ok(summaryDto);
    }
}
