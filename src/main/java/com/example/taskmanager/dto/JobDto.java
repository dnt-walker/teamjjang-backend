package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.domain.Task;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
        return JobDto.builder()
                .id(job.getId())
                .name(job.getName())
                .assignedUser(job.getAssignedUser())
                .description(job.getDescription())
                .startTime(job.getStartTime())
                .endTime(job.getEndTime())
                .completionTime(job.getCompletionTime())
                .completed(job.isCompleted())
                .build();
    }

    public Job toEntity(Task task) {
        return Job.builder()
                .id(this.id)
                .task(task)
                .name(this.name)
                .assignedUser(this.assignedUser)
                .description(this.description)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .completionTime(this.completionTime)
                .completed(this.completed)
                .build();
    }
}
