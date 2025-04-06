package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "태스크(Task)에 대한 데이터 전송 객체")
public class TaskDto {

    @Schema(description = "태스크 ID", example = "10")
    private Long id;

    @Schema(description = "태스크명", example = "프론트엔드 구현")
    private String name;

    @Schema(description = "생성자", example = "admin")
    private String creator;

    @Schema(description = "태스크 설명", example = "프론트엔드 화면 구성 및 로직 처리")
    private String description;

    @Schema(description = "시작일", example = "2025-04-01")
    private LocalDate startDate;

    @Schema(description = "종료 예정일", example = "2025-04-15")
    private LocalDate plannedEndDate;

    @Schema(description = "완료일", example = "2025-04-14")
    private LocalDate completionDate;

    private Set<String> assignees = new HashSet<>();

    private ProjectDto project;
    public static TaskDto from(Task task) {

        return new TaskDto(
            task.getId(),
            task.getName(),
            task.getCreator(),
            task.getDescription(),
            task.getStartDate(),
            task.getPlannedEndDate(),
            task.getCompletionDate() ,
            task.getAssignees(),
                null
        );
    }

    public static TaskDto fromWithProject(Task task) {

        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getCreator(),
                task.getDescription(),
                task.getStartDate(),
                task.getPlannedEndDate(),
                task.getCompletionDate() ,
                task.getAssignees(),
                ProjectDto.from(task.getProject())
        );
    }

    public Task toEntity() {
        return new Task(
            this.id,
            this.name,
            this.startDate,
            this.plannedEndDate,
            this.creator,
            this.description,
            this.assignees
        );
    }
}
