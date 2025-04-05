package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Task;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private Long projectId;
    private String name;
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private LocalDate completionDate;
    private String creator;
    private String description;
    private Set<String> assignees;
    private List<JobDto> jobs;

    public static TaskDto from(Task task) {
        List<JobDto> jobDtos = null;
        if (task.getJobs() != null && !task.getJobs().isEmpty()) {
            jobDtos = task.getJobs().stream()
                .map(JobDto::from)
                .collect(Collectors.toList());
        }
        
        return new TaskDto(
            task.getId(),
            task.getProject() != null ? task.getProject().getId() : null,
            task.getName(),
            task.getStartDate(),
            task.getPlannedEndDate(),
            task.getCompletionDate(),
            task.getCreator(),
            task.getDescription(),
            task.getAssignees(),
            jobDtos
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
