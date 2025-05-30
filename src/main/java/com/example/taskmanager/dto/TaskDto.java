package com.example.taskmanager.dto;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskAssignedUser;
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
@Schema(description = "태스크(Task)에 대한 데이터 전송 객체")
public class TaskDto extends ModifiedDto implements Serializable {

    @Schema(description = "태스크 ID", example = "10")
    private Long id;

    @Schema(description = "태스크명", example = "프론트엔드 구현")
    private String name;

    @Schema(description = "태스크 설명", example = "프론트엔드 화면 구성 및 로직 처리")
    private String description;

    @Schema(description = "시작일", example = "2025-04-01")
    private LocalDate startDate;

    @Schema(description = "종료 예정일", example = "2025-04-15")
    private LocalDate plannedEndDate;

    @Schema(description = "완료일", example = "2025-04-14")
    private LocalDate completionDate;

    @Schema(description = "태스크 상태", example = "CREATED")
    private JobStatus status;

    @Schema(description = "할당된 사용자 목록")
    @Builder.Default
    private Set<UserDto.UserSummaryDto> assignees = new HashSet<>();

    @Schema(description = "소속 프로젝트")
    private ProjectDto.ProjectSummaryDto project;

    // 태스크 요약 정보만 포함하는 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaskSummaryDto {
        private Long id;
        private String name;

        public static TaskSummaryDto from(Task task) {
            if (task == null) return null;

            return TaskSummaryDto.builder()
                    .id(task.getId())
                    .name(task.getName())
                    .build();
        }
    }

    public static TaskDto from(Task task) {
        if (task == null) return null;

        TaskDto dto = TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .plannedEndDate(task.getPlannedEndDate())
                .completionDate(task.getCompletionDate())
                .build();

        dto.setStatus(task.getStatus());

        // 프로젝트 정보 설정
        if (task.getProject() != null) {
            dto.setProject(ProjectDto.ProjectSummaryDto.from(task.getProject()));
        }

        dto.assignees = task.getAssignedUsers().stream()
                .map(pau -> UserDto.UserSummaryDto.from(pau.getUser()))
                .collect(Collectors.toSet());

        // 수정 정보 설정 (ModifiedDto 상속)
        if (task.getModifiedDate() != null) {
            dto.setModifiedDate(task.getModifiedDate().toString());
        }
        if (task.getModifier() != null) {
            dto.setModifier(UserDto.UserSummaryDto.from(task.getModifier()));
        }
        if (task.getRegisteredDate() != null) {
            dto.setRegisteredDate(task.getRegisteredDate().toString());
        }
        if (task.getRegister() != null) {
            dto.setRegister(UserDto.UserSummaryDto.from(task.getRegister()));
        }

        return dto;
    }

    // 할당된 사용자 정보 추가 메서드 - 서비스 계층에서 호출
    public void addAssignedUsers(Set<TaskAssignedUser> taskAssignedUsers) {
        if (taskAssignedUsers != null) {
            this.assignees = taskAssignedUsers.stream()
                    .map(tau -> UserDto.UserSummaryDto.from(tau.getUser()))
                    .collect(Collectors.toSet());
        }
    }

    public Task toEntity(Project project) {
        // 기본적으로 status 필드는 TaskDto에 포함되어 있지 않으므로, null로 설정하거나 기본값(Status.CREATED 등)으로 지정할 수 있습니다.
        // 여기서는 TaskDto에 추가한 status 값을 사용합니다.
        return new Task(
                this.id,
                project,
                this.name,
                this.startDate,
                this.plannedEndDate,
                this.completionDate,
                this.description,
                this.status
        );
    }

    public Task toEntity(Function<Long, Project> getProject) {
        // 기본적으로 status 필드는 TaskDto에 포함되어 있지 않으므로, null로 설정하거나 기본값(Status.CREATED 등)으로 지정할 수 있습니다.
        // 여기서는 TaskDto에 추가한 status 값을 사용합니다.
        return new Task(
            this.id,
            getProject.apply(project.getId()),
            this.name,
            this.startDate,
            this.plannedEndDate,
            this.completionDate,
            this.description,
            this.status
        );
    }
}
