package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.service.ProjectService;
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
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "프로젝트 관리 API")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "모든 프로젝트 조회", description = "시스템에 등록된 모든 프로젝트를 조회합니다.")
    public ResponseEntity<List<ProjectDto>> getProjects(
            @RequestParam(required = false) @Parameter(description = "활성 상태 필터링 (true: 활성, false: 비활성)") Boolean active,
            @RequestParam(required = false) @Parameter(description = "프로젝트 관리자 필터링") String manager,
            @RequestParam(required = false) @Parameter(description = "태스크 존재 여부 필터링 (true: 태스크 있음, false: 태스크 없음)") Boolean hasTasks,
            @RequestParam(required = false) @Parameter(description = "예정된 프로젝트 필터링 (true: 시작일이 현재 이후)") Boolean upcoming,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // QueryDSL 동적 검색 기반 처리
        List<ProjectDto> filteredProjects = projectService.searchProjects(active, manager, hasTasks, upcoming);
        return ResponseEntity.ok(filteredProjects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "프로젝트 상세 조회", description = "특정 ID의 프로젝트 정보를 조회합니다.")
    public ResponseEntity<ProjectDto> getProject(
            @PathVariable @Parameter(description = "프로젝트 ID") Long id,
//            @RequestParam(defaultValue = "false") @Parameter(description = "태스크 포함 여부 (true: 태스크 포함, false: 태스크 미포함)") boolean expand,
            @AuthenticationPrincipal UserDetails userDetails) {
        // expand 파라미터로 관련 리소스 확장 여부 결정
//        ProjectDto project = expand
//            ? projectService.getProjectWithTasksById(id)
//            : projectService.getProjectById(id);
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생성할 프로젝트 정보") ProjectDto projectDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        ProjectDto createdProject = projectService.createProject(projectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "프로젝트 전체 수정", description = "특정 ID의 프로젝트 정보를 전체 수정합니다.")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable @Parameter(description = "수정할 프로젝트 ID") Long id,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정된 프로젝트 정보") ProjectDto projectDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        ProjectDto updatedProject = projectService.updateProject(id, projectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "프로젝트 상태 변경", description = "특정 ID의 프로젝트 상태를 변경합니다.")
    public ResponseEntity<ProjectDto> updateProjectStatus(
            @PathVariable @Parameter(description = "상태를 변경할 프로젝트 ID") Long id,
            @RequestParam(required = true) @Parameter(description = "프로젝트 완료 여부 (true: 완료, false: 활성화)") boolean completed,
            @AuthenticationPrincipal UserDetails userDetails) {

        ProjectDto updatedProject = completed
            ? projectService.completeProject(id)
            : projectService.activateProject(id); // 활성화 메서드 추가 필요

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "프로젝트 삭제", description = "특정 ID의 프로젝트를 삭제합니다.")
    public ResponseEntity<Void> deleteProject(
            @PathVariable @Parameter(description = "삭제할 프로젝트 ID") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}