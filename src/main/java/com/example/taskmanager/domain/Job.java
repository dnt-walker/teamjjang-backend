package com.example.taskmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
