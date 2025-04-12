package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.security.TeamUserDetails;
import com.example.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "프로젝트 관리 API")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "전체 프로젝트 목록 조회")
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getProjectList() {
        return ResponseEntity.ok(projectService.getProjectList());
    }

    @Operation(summary = "프로젝트 생성")
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "프로젝트 정보")
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProjectWithUsers(projectDto));
    }

    @Operation(summary = "프로젝트 단건 조회")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProject(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @Operation(summary = "프로젝트 수정")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 프로젝트 정보")
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(ProjectDto.from(projectService.updateProjectWithUsers(projectId, projectDto)));
    }

    @Operation(summary = "프로젝트 삭제")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
