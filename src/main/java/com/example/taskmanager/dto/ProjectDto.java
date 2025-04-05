package com.example.taskmanager.dto;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.Task;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String manager;
    private boolean active;
    private List<TaskDto> tasks;
    
    // 프로젝트 엔티티로부터 DTO 생성 - 태스크 포함
    public static ProjectDto fromWithTasks(Project project) {
        List<TaskDto> taskDtos = null;
        if (project.getTasks() != null && !project.getTasks().isEmpty()) {
            taskDtos = project.getTasks().stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
        }
        
        return new ProjectDto(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            project.getManager(),
            project.isActive(),
            taskDtos
        );
    }
    
    // 프로젝트 엔티티로부터 DTO 생성 - 태스크 미포함
    public static ProjectDto from(Project project) {
        return new ProjectDto(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            project.getManager(),
            project.isActive(),
            null
        );
    }
    
    // DTO를 엔티티로 변환 - 새 프로젝트 생성용
    public Project toEntity() {
        return new Project(
            this.id,
            this.name,
            this.description,
            this.startDate,
            this.endDate, 
            this.manager,
            this.active
        );
    }
}
