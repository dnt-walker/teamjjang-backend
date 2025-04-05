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
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
    
    private String name;
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private LocalDate completionDate;
    private String creator;

    @Lob
    private String description;

    @ElementCollection
    @CollectionTable(name = "task_assignees", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "assignee")
    private Set<String> assignees = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();
    
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
    
    // 프로젝트 설정 메서드 (Project 엔티티와의 연결을 위한 추가)
    public void setProject(Project project) {
        // 기존 프로젝트에서 제거
        if (this.project != null) {
            this.project.removeTask(this);
        }
        
        this.project = project;
        
        // 새 프로젝트에 추가
        if (project != null && !project.getTasks().contains(this)) {
            project.addTask(this);
        }
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
    
    // Job 추가 메서드
    public void addJob(Job job) {
        this.jobs.add(job);
        job.setTask(this);
    }
    
    // Job 제거 메서드
    public void removeJob(Job job) {
        this.jobs.remove(job);
        job.setTask(null);
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
