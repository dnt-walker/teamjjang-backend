package com.example.taskmanager.domain;

import com.example.taskmanager.dto.Status;
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
    @Column(name = "job_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "name", length = 128)
    private String name;
    
    @Column(name = "assigned_user", length = 128)
    private String assignedUser;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "completion_time")
    private LocalDateTime completionTime;
    
    @Column(name = "completed")
    private boolean completed;
    
    @Column(name = "status", length = 1)
    @Convert(converter = StatusConverter.class)
    private Status status;
    
    public Job(Long id, Task task, String name, String assignedUser, String description,
              LocalDateTime startTime, LocalDateTime endTime, LocalDateTime completionTime, boolean completed, Status status) {
        this.id = id;
        this.task = task;
        this.name = name != null ? name : "";
        this.assignedUser = assignedUser;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.completionTime = completionTime;
        this.completed = completed;
        this.status = status != null ? status : Status.CREATED;
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
    
    // 상태 설정 메서드
    public void setStatus(Status status) {
        this.status = status;
    }
    
    // 작업 상태에 따른 완료 여부 업데이트
    public void updateCompletionByStatus() {
        if (this.status == Status.SUCCEEDED || 
            this.status == Status.FINISHED ||
            this.status == Status.FAILED) {
            this.complete();
        } else {
            this.reopen();
        }
    }
    
    // 상태 확인 메서드
    public boolean isCompleted() {
        return this.completed;
    }
    
    // 상태 확인 메서드
    public boolean isInProgress() {
        return Status.IN_PROGRESS.equals(this.status);
    }
    
    // 상태 확인 메서드
    public boolean isWaiting() {
        return Status.WAITING.equals(this.status);
    }
    
    // 상태 확인 메서드
    public boolean isCreated() {
        return Status.CREATED.equals(this.status);
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
