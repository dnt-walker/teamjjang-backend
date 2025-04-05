package com.example.taskmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    private String name;
    private String assignedUser;

    @Lob
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime completionTime;
    private boolean completed;
    
    public Job(Long id, Task task, String name, String assignedUser, String description,
              LocalDateTime startTime, LocalDateTime endTime, LocalDateTime completionTime, boolean completed) {
        this.id = id;
        this.task = task;
        this.name = name != null ? name : "";
        this.assignedUser = assignedUser;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.completionTime = completionTime;
        this.completed = completed;
    }
    
    // Task 설정 메서드 (양방향 관계 관리용)
    void setTask(Task task) {
        this.task = task;
    }
    
    // 완료 상태로 변경하는 메서드
    public void complete() {
        this.completed = true;
        this.completionTime = LocalDateTime.now();
    }
    
    // 재오픈 메서드
    public void reopen() {
        this.completed = false;
        this.completionTime = null;
    }
    
    // 속성 업데이트 메서드
    public void update(String name, String assignedUser, String description, 
                      LocalDateTime startTime, LocalDateTime endTime) {
        if (name != null) this.name = name;
        if (assignedUser != null) this.assignedUser = assignedUser;
        if (description != null) this.description = description;
        if (startTime != null) this.startTime = startTime;
        if (endTime != null) this.endTime = endTime;
    }
}
