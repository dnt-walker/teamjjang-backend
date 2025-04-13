package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.dto.ProjectFilterDto;
import com.example.taskmanager.dto.StatusSummaryDto;
import com.example.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "프로젝트 관리 API")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "전체 프로젝트 목록 조회")
    @GetMapping("/api/projects")
    public ResponseEntity<List<ProjectDto>> getProjectList(
//            @ParameterObject
//            @ModelAttribute
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC)
            Pageable pageable,
            @ParameterObject
            @ModelAttribute ProjectFilterDto filterDto
    ) {
        return ResponseEntity.ok(projectService.getProjectList(pageable, filterDto));
    }

    @GetMapping("/api/project-summaries")
    public ResponseEntity<StatusSummaryDto> getProjectSummary() {
        return ResponseEntity.ok(projectService.getProjectSummary());
    }

    @Operation(summary = "프로젝트 생성")
    @PostMapping("/api/projects")
    public ResponseEntity<ProjectDto> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "프로젝트 정보")
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProjectWithUsers(projectDto));
    }

    @Operation(summary = "프로젝트 단건 조회")
    @GetMapping("/api/projects/{projectId}")
    public ResponseEntity<ProjectDto> getProject(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @Operation(summary = "프로젝트 수정")
    @PutMapping("/api/projects/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 프로젝트 정보")
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(ProjectDto.from(projectService.updateProjectWithUsers(projectId, projectDto)));
    }

    @Operation(summary = "프로젝트 삭제")
    @DeleteMapping("/api/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "프로젝트의 Task의 상태 조회")
    @GetMapping("/api/projects/{projectId}/summaries")
    public ResponseEntity<StatusSummaryDto> getTaskSummary(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getTaskSummary(projectId));
    }

    @Operation(summary = "프로젝트의 Task의 상태 조회")
    @GetMapping("/api/projects/{projectId}/user-summaries")
    public ResponseEntity<StatusSummaryDto> getUserTaskSummary(
            @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getUserTaskSummary(userDetails, projectId));
    }
}
