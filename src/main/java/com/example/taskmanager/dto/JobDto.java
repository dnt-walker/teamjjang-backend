package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Schema(description = "담당자 목록", example = "['john.doe', 'jane.smith']")
    private Set<String> assignedUsers = new HashSet<>();

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
    
    @Schema(description = "작업 상태", example = "IN_PROGRESS")
    private Status status;

    // 이전 버전과의 호환성을 위한 필드
    @Deprecated
    @Schema(description = "단일 담당자 (하위 호환용, 새로운 구현에서는 assignedUsers 사용)", example = "johndoe", deprecated = true)
    private String assignedUser;

    public static JobDto from(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setAssignedUsers(job.getAssignedUsers());
        // 이전 버전과의 호환성을 위해 첫 번째 담당자를 assignedUser에 설정
        if (!job.getAssignedUsers().isEmpty()) {
            dto.setAssignedUser(job.getAssignedUsers().iterator().next());
        }
        dto.setDescription(job.getDescription());
        dto.setStartTime(job.getStartTime());
        dto.setEndTime(job.getEndTime());
        dto.setCompletionTime(job.getCompletionTime());
        dto.setCompleted(job.isCompleted());
        dto.setStatus(job.getStatus());
        return dto;
    }

    public Job toEntity(Task task) {
        Set<String> users = new HashSet<>(this.assignedUsers);
        
        // 이전 버전과의 호환성: assignedUser가 설정되어 있고 assignedUsers가 비어있으면 추가
        if (this.assignedUser != null && !this.assignedUser.isEmpty() && this.assignedUsers.isEmpty()) {
            users.add(this.assignedUser);
        }
        
        return new Job(
            this.id,
            task,
            this.name,
            users,
            this.description,
            this.startTime,
            this.endTime,
            this.completionTime,
            this.completed,
            this.status != null ? this.status : Status.CREATED
        );
    }
}