package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "프로젝트(Project)에 대한 데이터 전송 객체")
public class ProjectDto {

    @Schema(description = "프로젝트 ID", example = "100")
    private Long id;

    @Schema(description = "프로젝트명", example = "신규 서비스 런칭")
    private String name;

    @Schema(description = "설명", example = "2025년 신규 웹 플랫폼 개발 프로젝트")
    private String description;

    @Schema(description = "프로젝트 매니저", example = "alice")
    private String manager;

    @Schema(description = "시작일", example = "2025-03-01")
    private LocalDate startDate;

    @Schema(description = "종료일", example = "2025-06-30")
    private LocalDate endDate;

    @Schema(description = "활성 상태", example = "true")
    private boolean active;

    // 프로젝트 엔티티로부터 DTO 생성 - 태스크 미포함
    public static ProjectDto from(Project project) {
        return new ProjectDto(
            project.getId(),
            project.getName(),
            project.getDescription(),
                project.getManager(),
            project.getStartDate(),
            project.getEndDate(),
            project.isActive()
        );
    }
    
    // DTO를 엔티티로 변환 - 새 프로젝트 생성용
    public Project toEntity() {
        return new Project(
            this.id,
            this.name,
            this.description,
            this.startDate,
            this.endDate, 
            this.manager,
            this.active
        );
    }
}
