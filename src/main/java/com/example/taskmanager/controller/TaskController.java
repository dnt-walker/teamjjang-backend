package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "태스크", description = "태스크 관리 API")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "모든 태스크 조회", description = "모든 태스크 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTaskDtos();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "태스크 상세 조회", description = "ID로 특정 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "404", description = "태스크를 찾을 수 없음")
    public ResponseEntity<TaskDto> getTask(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long id) {
        TaskDto task = taskService.getTaskDtoById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @Operation(summary = "태스크 생성 또는 수정", description = "새 태스크를 생성하거나 기존 태스크를 수정합니다")
    @ApiResponse(responseCode = "200", description = "태스크 생성/수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<TaskDto> saveTask(
            @Parameter(description = "생성 또는 수정할 태스크 정보", required = true)
            @RequestBody TaskDto taskDto) {
        TaskDto saved = taskService.saveTask(taskDto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "태스크 삭제", description = "ID로 태스크를 삭제합니다")
    @ApiResponse(responseCode = "204", description = "태스크 삭제 성공")
    @ApiResponse(responseCode = "404", description = "태스크를 찾을 수 없음")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long id) {
        taskService.removeTask(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/creator/{creator}")
    @Operation(summary = "생성자별 태스크 조회", description = "특정 사용자가 생성한 모든 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getTasksByCreator(
            @Parameter(description = "생성자 사용자명", required = true)
            @PathVariable String creator) {
        List<TaskDto> tasks = taskService.getTasksByCreator(creator);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/assignee/{assignee}")
    @Operation(summary = "담당자별 태스크 조회", description = "특정 담당자에게 할당된 모든 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getTasksByAssignee(
            @Parameter(description = "담당자 사용자명", required = true)
            @PathVariable String assignee) {
        List<TaskDto> tasks = taskService.getTasksByAssignee(assignee);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/overdue")
    @Operation(summary = "기한 지난 태스크 조회", description = "예정 종료일이 지났으나 미완료된 모든 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getOverdueTasks() {
        List<TaskDto> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/incomplete")
    @Operation(summary = "미완료 태스크 조회", description = "아직 완료되지 않은 모든 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getIncompleteTasks() {
        List<TaskDto> tasks = taskService.getIncompleteTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/completed")
    @Operation(summary = "완료된 태스크 조회", description = "완료된 모든 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getCompletedTasks() {
        List<TaskDto> tasks = taskService.getCompletedTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/active-on-date")
    @Operation(summary = "특정 날짜의 활성 태스크 조회", description = "특정 날짜에 활성 상태인 모든 태스크를 조회합니다")
    @ApiResponse(responseCode = "200", description = "태스크 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> getActiveTasksOnDate(
            @Parameter(description = "날짜 (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TaskDto> tasks = taskService.getActiveTasksOnDate(date);
        return ResponseEntity.ok(tasks);
    }
    
    @PutMapping("/{id}/complete")
    @Operation(summary = "태스크 완료 처리", description = "태스크를 완료 상태로 변경합니다")
    @ApiResponse(responseCode = "200", description = "태스크 완료 처리 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "404", description = "태스크를 찾을 수 없음")
    public ResponseEntity<TaskDto> completeTask(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long id) {
        TaskDto task = taskService.completeTask(id);
        return ResponseEntity.ok(task);
    }
    
    @PutMapping("/{id}/assignees/add")
    @Operation(summary = "태스크 담당자 추가", description = "태스크에 담당자를 추가합니다")
    @ApiResponse(responseCode = "200", description = "담당자 추가 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "404", description = "태스크를 찾을 수 없음")
    public ResponseEntity<TaskDto> addAssignee(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "담당자 사용자명", required = true)
            @RequestParam String assignee) {
        TaskDto task = taskService.addAssignee(id, assignee);
        return ResponseEntity.ok(task);
    }
    
    @PutMapping("/{id}/assignees/remove")
    @Operation(summary = "태스크 담당자 제거", description = "태스크에서 담당자를 제거합니다")
    @ApiResponse(responseCode = "200", description = "담당자 제거 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "404", description = "태스크를 찾을 수 없음")
    public ResponseEntity<TaskDto> removeAssignee(
            @Parameter(description = "태스크 ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "담당자 사용자명", required = true)
            @RequestParam String assignee) {
        TaskDto task = taskService.removeAssignee(id, assignee);
        return ResponseEntity.ok(task);
    }
    
    @GetMapping("/search")
    @Operation(summary = "태스크 검색", description = "다양한 조건으로 태스크를 검색합니다")
    @ApiResponse(responseCode = "200", description = "태스크 검색 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class)))
    public ResponseEntity<List<TaskDto>> searchTasks(
            @Parameter(description = "검색어 (이름 또는 설명에 포함)")
            @RequestParam(required = false) String keyword,
            
            @Parameter(description = "생성자")
            @RequestParam(required = false) String creator,
            
            @Parameter(description = "담당자")
            @RequestParam(required = false) String assignee,
            
            @Parameter(description = "완료 상태")
            @RequestParam(required = false) Boolean isCompleted,
            
            @Parameter(description = "시작일 검색 시작값 (포함)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            
            @Parameter(description = "시작일 검색 종료값 (포함)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            
            @Parameter(description = "종료일 검색 시작값 (포함)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            
            @Parameter(description = "종료일 검색 종료값 (포함)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo) {
        
        List<TaskDto> tasks = taskService.searchTasks(
                keyword, creator, assignee, isCompleted,
                startDateFrom, startDateTo, endDateFrom, endDateTo);
        
        return ResponseEntity.ok(tasks);
    }
}
