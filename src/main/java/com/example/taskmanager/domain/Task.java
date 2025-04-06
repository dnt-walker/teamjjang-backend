package com.example.taskmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
    
    @Column(name = "name", length = 128)
    private String name;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;
    
    @Column(name = "completion_date")
    private LocalDate completionDate;
    
    @Column(name = "creator", length = 128)
    private String creator;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "task_assignees", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "assignee")
    private Set<String> assignees = new HashSet<>();
    
    public Task(Long id, String name, LocalDate startDate, LocalDate plannedEndDate, 
                String creator, String description, Set<String> assignees) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.plannedEndDate = plannedEndDate;
        this.creator = creator;
        this.description = description;
        
        if (assignees != null) {
            this.assignees = new HashSet<>(assignees);
        }
    }
    
    // Project 관련 메서드 수정 - 단방향 관계로 변경
    public void setProject(Project project) {
        this.project = project;
    }
    
    // 완료일 설정 메서드
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
    
    // 완료 처리 메서드
    public void complete() {
        this.completionDate = LocalDate.now();
    }
    
    // 재오픈 메서드
    public void reopen() {
        this.completionDate = null;
    }
    
    // 담당자 추가 메서드
    public void addAssignee(String assignee) {
        if (this.assignees == null) {
            this.assignees = new HashSet<>();
        }
        this.assignees.add(assignee);
    }
    
    // 담당자 제거 메서드
    public void removeAssignee(String assignee) {
        if (this.assignees != null) {
            this.assignees.remove(assignee);
        }
    }
    
    // 속성 업데이트 메서드
    public void update(String name, String description, LocalDate startDate, 
                      LocalDate plannedEndDate, Set<String> assignees) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (startDate != null) this.startDate = startDate;
        if (plannedEndDate != null) this.plannedEndDate = plannedEndDate;
        if (assignees != null) this.assignees = assignees;
    }
}
