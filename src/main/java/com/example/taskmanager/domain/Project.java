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
    @Column(name = "project_id")
    private Long id;
    
    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "manager", length = 128)
    private String manager;
    
    @Column(name = "active")
    private boolean active;
    

    
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
    
    // Project의 Tasks 관련 메서드 제거
    // 기존 양방향 관계를 단방향으로 변경
    
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
