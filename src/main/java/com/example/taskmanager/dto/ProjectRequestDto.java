package com.example.taskmanager.dto;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "프로젝트(Project)에 대한 데이터 전송 객체")
public class ProjectRequestDto extends ModifiedDto implements Serializable {

    @Schema(description = "프로젝트 ID", example = "100")
    private Long id;

    @Schema(description = "프로젝트명", example = "신규 서비스 런칭")
    private String name;

    @Schema(description = "설명", example = "2025년 신규 웹 플랫폼 개발 프로젝트")
    private String description;

    @Schema(description = "프로젝트 매니저")
    private Long managerId;

    @Schema(description = "시작일", example = "2025-03-01")
    private LocalDate startDate;

    @Schema(description = "종료일", example = "2025-06-30")
    private LocalDate endDate;

    private JobStatus status;

    @Schema(description = "할당된 사용자 목록")
    @Builder.Default
    private Set<Long> assignees = new HashSet<>();

    // DTO를 엔티티로 변환 - 새 프로젝트 생성용
    public Project toEntity(User manager) {
        // Check if manager is not null; if null, set managerEntity to null to avoid NPE
//        User managerEntity = (this.manager != null) ? getUser.apply(this.manager.getId()) : null;
        return new Project(
                this.id,
                this.name,
                this.description,
                this.startDate,
                this.endDate,
                manager
        );
    }

    public Project toEntity(Function<Long, User> getUser) {
        // Check if manager is not null; if null, set managerEntity to null to avoid NPE
        User managerEntity = (this.managerId != null) ? getUser.apply(this.managerId) : null;
        return new Project(
                this.id,
                this.name,
                this.description,
                this.startDate,
                this.endDate,
                managerEntity
        );
    }
}
