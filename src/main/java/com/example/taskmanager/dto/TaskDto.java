package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Task;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private LocalDate completionDate;
    private String creator;
    private String description;
    private Set<String> assignees;
    private List<JobDto> jobs;

    public static TaskDto from(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .startDate(task.getStartDate())
                .plannedEndDate(task.getPlannedEndDate())
                .completionDate(task.getCompletionDate())
                .creator(task.getCreator())
                .description(task.getDescription())
                .assignees(task.getAssignees())
                .jobs(task.getJobs() != null ? task.getJobs().stream().map(JobDto::from).collect(Collectors.toList()) : null)
                .build();
    }

    public Task toEntity() {
        return Task.builder()
                .id(this.id)
                .name(this.name)
                .startDate(this.startDate)
                .plannedEndDate(this.plannedEndDate)
                .completionDate(this.completionDate)
                .creator(this.creator)
                .description(this.description)
                .assignees(this.assignees)
                .build();
    }
}