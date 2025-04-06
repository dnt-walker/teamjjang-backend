package com.example.taskmanager.controller;

import com.example.taskmanager.dto.Status;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskService;
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
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "태스크 관리 API")
public class TaskController {
    private final TaskService taskService;

    // 모든 태스크 조회 (프로젝트와 무관)
    @GetMapping("/api/tasks")
    @Operation(summary = "모든 태스크 목록 조회", description = "모든 태스크 목록을 조회합니다.")
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @RequestParam(required = false) @Parameter(description = "태스크 완료 여부 필터링") Boolean completed,
            @RequestParam(required = false) @Parameter(description = "태스크 담당자 필터링") String assignee,
            @RequestParam(required = false) @Parameter(description = "태스크 생성자 필터링") String creator,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // QueryDSL을 사용한 동적 검색
        List<TaskDto> tasks = taskService.searchTasks(completed, assignee, creator);
        return ResponseEntity.ok(tasks);
    }

    // 프로젝트 내 태스크 조회
    @GetMapping("/api/projects/{projectId}/tasks")
    @Operation(summary = "프로젝트의 태스크 목록 조회", description = "특정 프로젝트에 속한 태스크를 조회합니다. JobStatus를 사용하여 태스크 상태별로 필터링할 수 있습니다.")
    public ResponseEntity<List<TaskDto>> getProjectTasks(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @RequestParam(required = false) @Parameter(description = "태스크 상태 필터링 (CREATED: 생성됨, WAITING: 대기 중, IN_PROGRESS: 진행 중, SUCCEEDED/FINISHED: 완료됨, FAILED: 실패)") Status status,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<TaskDto> tasks = taskService.getTasksByProjectId(projectId, status);
        
        return ResponseEntity.ok(tasks);
    }
    
    // 프로젝트에 태스크 추가
    @PostMapping("/api/projects/{projectId}/tasks")
    @Operation(summary = "프로젝트에 태스크 추가", description = "특정 프로젝트에 새로운 태스크를 추가합니다.")
    public ResponseEntity<TaskDto> addTaskToProject(
            @PathVariable @Parameter(description = "태스크를 추가할 프로젝트 ID") Long projectId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가할 태스크 정보") TaskDto taskDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TaskDto createdTask = taskService.addTaskToProject(projectId, taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    // 프로젝트의 특정 태스크 조회
    @GetMapping("/api/projects/{projectId}/tasks/{taskId}")
    @Operation(summary = "프로젝트의 특정 태스크 조회", description = "특정 프로젝트에 속한 특정 태스크의 상세 정보를 조회합니다.")
    public ResponseEntity<TaskDto> getProjectTask(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "조회할 태스크 ID") Long taskId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TaskDto task = taskService.getTaskFromProject(projectId, taskId);
        return ResponseEntity.ok(task);
    }
    
    // 프로젝트의 특정 태스크 수정
    @PutMapping("/api/projects/{projectId}/tasks/{taskId}")
    @Operation(summary = "프로젝트의 태스크 수정", description = "프로젝트에 속한 특정 태스크의 정보를 업데이트합니다.")
    public ResponseEntity<TaskDto> updateProjectTask(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @PathVariable @Parameter(description = "수정할 태스크 ID") Long taskId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 태스크 정보") TaskDto taskDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TaskDto updatedTask = taskService.updateProjectTask(projectId, taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }
    
    // 프로젝트에서 태스크 제거
    @DeleteMapping("/api/projects/{projectId}/tasks/{taskId}")
    @Operation(summary = "프로젝트에서 태스크 제거", description = "특정 프로젝트에서 태스크를 제거합니다.")
    public ResponseEntity<Void> removeTaskFromProject(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId, 
            @PathVariable @Parameter(description = "제거할 태스크 ID") Long taskId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        taskService.removeTaskFromProject(projectId, taskId);
        return ResponseEntity.noContent().build();
    }
}
