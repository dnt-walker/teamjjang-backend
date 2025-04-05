package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.JobDto;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "태스크 관리 API")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "태스크 목록 조회", description = "모든 태스크 목록을 조회합니다.")
    public ResponseEntity<List<TaskDto>> getTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String creator) {
        
        // 필터링 로직 - 서비스에 해당 메서드들 추가 필요
        if (Boolean.TRUE.equals(completed)) {
            return ResponseEntity.ok(taskService.getCompletedTasks());
        } else if (assignee != null && !assignee.isEmpty()) {
            return ResponseEntity.ok(taskService.getTasksByAssignee(assignee));
        } else if (creator != null && !creator.isEmpty()) {
            return ResponseEntity.ok(taskService.getTasksByCreator(creator));
        }
        
        return ResponseEntity.ok(taskService.getAllTaskDtos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "태스크 상세 조회", description = "특정 ID의 태스크 정보를 조회합니다.")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        TaskDto task = taskService.getTaskDtoById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @Operation(summary = "태스크 생성", description = "새로운 태스크를 생성합니다.")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        TaskDto saved = taskService.saveTask(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "태스크 전체 수정", description = "특정 ID의 태스크 정보를 전체 수정합니다.")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDto taskDto) {
        
        TaskDto updated = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "태스크 상태 변경", description = "특정 ID의 태스크 완료 상태를 변경합니다.")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam(required = true) boolean completed) {
        
        TaskDto updated = completed
            ? taskService.completeTask(id)
            : taskService.reopenTask(id); // 재오픈 메서드 추가 필요
        
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "태스크 삭제", description = "특정 ID의 태스크를 삭제합니다.")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.removeTask(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{taskId}/jobs")
    @Operation(summary = "태스크의 작업 목록 조회", description = "특정 태스크에 속한 모든 작업을 조회합니다.")
    public ResponseEntity<List<JobDto>> getTaskJobs(@PathVariable Long taskId) {
        TaskDto task = taskService.getTaskDtoById(taskId);
        return ResponseEntity.ok(task.getJobs());
    }
    
    @PostMapping("/{taskId}/jobs")
    @Operation(summary = "태스크에 작업 추가", description = "특정 태스크에 새로운 작업을 추가합니다.")
    public ResponseEntity<JobDto> addJobToTask(
            @PathVariable Long taskId,
            @RequestBody JobDto jobDto) {
        
        JobDto createdJob = taskService.addJobToTask(taskId, jobDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }
    
    @GetMapping("/{taskId}/jobs/{jobId}")
    @Operation(summary = "태스크의 특정 작업 조회", description = "특정 태스크에 속한 특정 작업의 상세 정보를 조회합니다.")
    public ResponseEntity<JobDto> getTaskJob(
            @PathVariable Long taskId,
            @PathVariable Long jobId) {
        
        JobDto job = taskService.getJobFromTask(taskId, jobId);
        return ResponseEntity.ok(job);
    }
    
    @PutMapping("/{taskId}/jobs/{jobId}")
    @Operation(summary = "태스크의 작업 수정", description = "특정 태스크에 속한 작업을 수정합니다.")
    public ResponseEntity<JobDto> updateTaskJob(
            @PathVariable Long taskId,
            @PathVariable Long jobId,
            @RequestBody JobDto jobDto) {
        
        JobDto updatedJob = taskService.updateJobInTask(taskId, jobId, jobDto);
        return ResponseEntity.ok(updatedJob);
    }
    
    @DeleteMapping("/{taskId}/jobs/{jobId}")
    @Operation(summary = "태스크에서 작업 제거", description = "특정 태스크에서 작업을 제거합니다.")
    public ResponseEntity<Void> removeJobFromTask(
            @PathVariable Long taskId,
            @PathVariable Long jobId) {
        
        taskService.removeJobFromTask(taskId, jobId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{taskId}/assignees")
    @Operation(summary = "태스크 담당자 관리", description = "특정 태스크의 담당자를 추가 또는 제거합니다.")
    public ResponseEntity<TaskDto> manageTaskAssignees(
            @PathVariable Long taskId,
            @RequestParam String assignee,
            @RequestParam Boolean add) {
        
        TaskDto updatedTask = Boolean.TRUE.equals(add)
            ? taskService.addAssigneeToTask(taskId, assignee)
            : taskService.removeAssigneeFromTask(taskId, assignee);
        
        return ResponseEntity.ok(updatedTask);
    }
}
