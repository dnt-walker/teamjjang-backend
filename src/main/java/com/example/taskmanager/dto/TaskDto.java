package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Task;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setStartDate(task.getStartDate());
        dto.setPlannedEndDate(task.getPlannedEndDate());
        dto.setCompletionDate(task.getCompletionDate());
        dto.setCreator(task.getCreator());
        dto.setDescription(task.getDescription());
        dto.setAssignees(task.getAssignees());
        
        if (task.getJobs() != null) {
            dto.setJobs(task.getJobs().stream()
                .map(JobDto::from)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public Task toEntity() {
        return new Task(
            this.id,
            this.name,
            this.startDate,
            this.plannedEndDate,
            this.creator,
            this.description,
            this.assignees != null ? this.assignees : new HashSet<>()
        );
    }
}