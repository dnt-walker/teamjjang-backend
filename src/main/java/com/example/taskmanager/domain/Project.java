package com.example.taskmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String manager;
    private boolean active;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    
    public Project(Long id, String name, String description, LocalDate startDate, 
                   LocalDate endDate, String manager, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.manager = manager;
        this.active = active;
    }
    
    // Task 추가 메서드
    public void addTask(Task task) {
        this.tasks.add(task);
        // Task에 project 설정은 Task 클래스 내에서 처리
    }
    
    // Task 제거 메서드
    public void removeTask(Task task) {
        this.tasks.remove(task);
        // Task에서 project 제거는 Task 클래스 내에서 처리
    }
    
    // 완료 처리 메서드
    public void complete() {
        this.active = false;
    }
    
    // 재활성화 메서드
    public void activate() {
        this.active = true;
    }
    
    // 속성 업데이트 메서드
    public void update(String name, String description, LocalDate startDate, 
                       LocalDate endDate, String manager) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (startDate != null) this.startDate = startDate;
        if (endDate != null) this.endDate = endDate;
        if (manager != null) this.manager = manager;
    }
}
