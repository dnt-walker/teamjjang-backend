package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "프로젝트 관리 API")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "모든 프로젝트 조회", description = "시스템에 등록된 모든 프로젝트를 조회합니다.")
    public ResponseEntity<List<ProjectDto>> getProjects(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String manager,
            @RequestParam(required = false) Boolean hasTasks,
            @RequestParam(required = false) Boolean upcoming) {
        
        // 필터 적용 - 쿼리 파라미터를 사용한 필터링
        if (Boolean.TRUE.equals(active)) {
            return ResponseEntity.ok(projectService.getActiveProjects());
        } else if (manager != null && !manager.isEmpty()) {
            return ResponseEntity.ok(projectService.getProjectsByManager(manager));
        } else if (Boolean.TRUE.equals(hasTasks)) {
            return ResponseEntity.ok(projectService.getProjectsWithTasks());
        } else if (Boolean.TRUE.equals(upcoming)) {
            return ResponseEntity.ok(projectService.getUpcomingProjects());
        }
        
        // 필터가 없으면 모든 프로젝트 반환
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "프로젝트 상세 조회", description = "특정 ID의 프로젝트 정보를 조회합니다.")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long id, 
                                               @RequestParam(defaultValue = "false") boolean expand) {
        // expand 파라미터로 관련 리소스 확장 여부 결정
        ProjectDto project = expand 
            ? projectService.getProjectWithTasksById(id)
            : projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
    
    @PostMapping
    @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = projectService.createProject(projectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "프로젝트 전체 수정", description = "특정 ID의 프로젝트 정보를 전체 수정합니다.")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, 
                                                  @RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = projectService.updateProject(id, projectDto);
        return ResponseEntity.ok(updatedProject);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "프로젝트 상태 변경", description = "특정 ID의 프로젝트 상태를 변경합니다.")
    public ResponseEntity<ProjectDto> updateProjectStatus(
            @PathVariable Long id,
            @RequestParam(required = true) boolean completed) {
        
        ProjectDto updatedProject = completed
            ? projectService.completeProject(id)
            : projectService.activateProject(id); // 활성화 메서드 추가 필요
        
        return ResponseEntity.ok(updatedProject);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "프로젝트 삭제", description = "특정 ID의 프로젝트를 삭제합니다.")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{projectId}/tasks")
    @Operation(summary = "프로젝트의 태스크 목록 조회", description = "특정 프로젝트에 속한 모든 태스크를 조회합니다.")
    public ResponseEntity<List<TaskDto>> getProjectTasks(@PathVariable Long projectId) {
        ProjectDto project = projectService.getProjectWithTasksById(projectId);
        return ResponseEntity.ok(project.getTasks());
    }
    
    @PostMapping("/{projectId}/tasks")
    @Operation(summary = "프로젝트에 태스크 추가", description = "특정 프로젝트에 새로운 태스크를 추가합니다.")
    public ResponseEntity<TaskDto> addTaskToProject(@PathVariable Long projectId, 
                                                  @RequestBody TaskDto taskDto) {
        ProjectDto updatedProject = projectService.addTaskToProject(projectId, taskDto);
        // 생성된 태스크만 반환
        TaskDto createdTask = updatedProject.getTasks().stream()
                .filter(t -> t.getName().equals(taskDto.getName()))
                .findFirst()
                .orElseThrow();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    @GetMapping("/{projectId}/tasks/{taskId}")
    @Operation(summary = "프로젝트의 특정 태스크 조회", description = "특정 프로젝트에 속한 특정 태스크의 상세 정보를 조회합니다.")
    public ResponseEntity<TaskDto> getProjectTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        
        TaskDto task = projectService.getTaskFromProject(projectId, taskId);
        return ResponseEntity.ok(task);
    }
    
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    @Operation(summary = "프로젝트에서 태스크 제거", description = "특정 프로젝트에서 태스크를 제거합니다.")
    public ResponseEntity<Void> removeTaskFromProject(
            @PathVariable Long projectId, 
            @PathVariable Long taskId) {
        
        projectService.removeTaskFromProject(projectId, taskId);
        return ResponseEntity.noContent().build();
    }
}
