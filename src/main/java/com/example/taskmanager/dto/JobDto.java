package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private Long id;
    private String name;
    private String assignedUser;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime completionTime;
    private boolean completed;

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