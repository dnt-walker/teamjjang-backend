package com.example.taskmanager.dto;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.SubTask;
import com.example.taskmanager.domain.SubTaskAssignedUser;
import com.example.taskmanager.domain.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "작업(Job)에 대한 데이터 전송 객체")
public class SubTaskDto extends ModifiedDto implements Serializable {

    @Schema(description = "작업 ID", example = "1")
    private Long id;

    @Schema(description = "작업명", example = "API 설계")
    private String name;

    @Schema(description = "담당자 목록")
    @Builder.Default
    private Set<UserDto.UserSummaryDto> assignees = new HashSet<>();

    @Schema(description = "작업 설명", example = "Spring Boot 기반 API 설계")
    private String description;

    @Schema(description = "작업 시작 시간", example = "2025-04-01T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "작업 종료 시간", example = "2025-04-03T18:00:00")
    private LocalDateTime endTime;

    @Schema(description = "작업 상태", example = "IN_PROGRESS")
    private JobStatus status;

    @Schema(description = "소속 태스크")
    private TaskDto.TaskSummaryDto task;

    public static SubTaskDto from(SubTask subTask) {
        if (subTask == null) return null;

        SubTaskDto dto =  SubTaskDto.builder()
                .id(subTask.getId())
                .name(subTask.getName())
                .description(subTask.getDescription())
                .startTime(subTask.getStartTime())
                .endTime(subTask.getEndTime())
                .status(subTask.getStatus())
                .task(TaskDto.TaskSummaryDto.from(subTask.getTask()))
                .assignees(subTask.getAssignedUsers() != null ?
                        subTask.getAssignedUsers().stream()
                                .map(asu -> UserDto.UserSummaryDto.from(asu.getUser()))
                                .collect(Collectors.toSet()) :
                        new HashSet<>()).build();
        dto.modifier= subTask.getModifier() != null ? UserDto.UserSummaryDto.from(subTask.getModifier()) : null;
        dto.modifiedDate = subTask.getModifiedDate() != null ? subTask.getModifiedDate().toString() : null;
        dto.registeredDate = subTask.getRegisteredDate() != null ? subTask.getRegisteredDate().toString() : null;
        dto.register = subTask.getRegister() != null ? UserDto.UserSummaryDto.from(subTask.getRegister()) : null;
        return dto;
    }

    public SubTask toEntity(Task task) {
        return new SubTask(
                this.id,
                this.name,
                task,
                this.description
        );
    }

    public SubTask toEntity(Function<Long, Task> getTask) {
        return new SubTask(
                this.id,
                this.name,
                getTask.apply(this.task.getId()),
                this.description
        );
    }
}
