package com.example.taskmanager.dto;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.ProjectAssignedUser;
import com.example.taskmanager.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "프로젝트(Project)에 대한 데이터 전송 객체")
public class ProjectDto extends ModifiedDto implements Serializable {

    @Schema(description = "프로젝트 ID", example = "100")
    private Long id;

    @Schema(description = "프로젝트명", example = "신규 서비스 런칭")
    private String name;

    @Schema(description = "설명", example = "2025년 신규 웹 플랫폼 개발 프로젝트")
    private String description;

    @Schema(description = "프로젝트 매니저")
    private UserDto.UserSummaryDto manager;

    @Schema(description = "시작일", example = "2025-03-01")
    private LocalDate startDate;

    @Schema(description = "종료일", example = "2025-06-30")
    private LocalDate endDate;

    @Schema(description = "전체 작업 가능 일수(Working DAY)", example = "10")
    private Long workingDays;

    @Schema(description = "프로젝트 경과 일수(지난 Working DAY)", example = "10")
    private Long usedWorkingDays;

    private JobStatus status;

    @Schema(description = "할당된 사용자 목록")
    @Builder.Default
    private Set<UserDto.UserSummaryDto> assignees = new HashSet<>();

    // 프로젝트 요약 정보만 포함하는 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectSummaryDto {
        private Long id;
        private String title;

        public static ProjectSummaryDto from(Project project) {
            if (project == null) return null;

            return ProjectSummaryDto.builder()
                    .id(project.getId())
                    .title(project.getName())
                    .build();
        }
    }


    // 프로젝트 엔티티로부터 DTO 생성 - 기본 정보만 포함
    public static ProjectDto from(Project project) {
        if (project == null) return null;

        ProjectDto dto = ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .build();

        dto.setWorkingDays(dto.workingDaysBetween(project.getStartDate(), project.getEndDate()));
        dto.setUsedWorkingDays(dto.elapsedWorkingDaysFrom(project.getStartDate()));

        // 매니저 정보 설정
        if (project.getManager() != null) {
            dto.setManager(UserDto.UserSummaryDto.from(project.getManager()));
        }

        dto.assignees = project.getAssignedUsers().stream()
                .map(pau -> UserDto.UserSummaryDto.from(pau.getUser()))
                .collect(Collectors.toSet());

        // 수정 정보 설정 (ModifiedDto 상속)
        if (project.getModifiedDate() != null) {
            dto.setModifiedDate(project.getModifiedDate().toString());
        }
        if (project.getModifier() != null) {
            dto.setModifier(UserDto.UserSummaryDto.from(project.getModifier()));
        }
        if (project.getRegisteredDate() != null) {
            dto.setRegisteredDate(project.getRegisteredDate().toString());
        }
        if (project.getRegister() != null) {
            dto.setRegister(UserDto.UserSummaryDto.from(project.getRegister()));
        }

        return dto;
    }

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
        User managerEntity = (this.manager != null) ? getUser.apply(this.manager.getId()) : null;
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
