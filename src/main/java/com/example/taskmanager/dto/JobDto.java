package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "작업(Job)에 대한 데이터 전송 객체")
public class JobDto {

    @Schema(description = "작업 ID", example = "1")
    private Long id;

    @Schema(description = "작업명", example = "API 설계")
    private String name;

    @Schema(description = "담당자", example = "johndoe")
    private String assignedUser;

    @Schema(description = "작업 설명", example = "Spring Boot 기반 API 설계")
    private String description;

    @Schema(description = "작업 시작 시간", example = "2025-04-01T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "작업 종료 시간", example = "2025-04-03T18:00:00")
    private LocalDateTime endTime;

    @Schema(description = "작업 완료 여부", example = "false")
    private boolean completed;

    @Schema(description = "작업 완료 시간", example = "2025-04-03T18:00:00")
    private LocalDateTime completionTime;

    public static JobDto from(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setAssignedUser(job.getAssignedUser());
        dto.setDescription(job.getDescription());
        dto.setStartTime(job.getStartTime());
        dto.setEndTime(job.getEndTime());
        dto.setCompletionTime(job.getCompletionTime());
        dto.setCompleted(job.isCompleted());
        return dto;
    }

    public Job toEntity(Task task) {
        return new Job(
            this.id,
            task,
            this.name,
            this.assignedUser,
            this.description,
            this.startTime,
            this.endTime,
            this.completionTime,
            this.completed
        );
    }
}