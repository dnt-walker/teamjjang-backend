package com.example.taskmanager.domain;

import com.example.taskmanager.domain.mappedentity.ModifiedEntity;
import com.example.taskmanager.constant.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubTask extends ModifiedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private Long id;

    @Column(name = "subtask_name", length = 128)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id",
            foreignKey = @ForeignKey(name = "FK_subtask_task_id")
//            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    private Task task;

    @Lob
    @Column(name = "subtask_desc")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "subtask_status", length = 2)
    @Convert(converter = TaskStatusConverter.class)
    private JobStatus status;

    @Setter
    @OneToMany(mappedBy = "subTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTaskAssignedUser> assignedUsers = new ArrayList<>();

    public SubTask(Long id, String name, Task task, String description
//                   LocalDateTime startTime,
//                   LocalDateTime endTime
//                   TaskStatus status
                   ) {
        this.id = id;
        this.name = name;
        this.task = task;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
        this.status = JobStatus.CREATED;
    }
    
    // 업데이트 메서드: 현재 Job 엔티티의 멤버 값을 업데이트합니다. 각 파라미터의 값이 null이 아니고 기존 값과 다를 경우 업데이트합니다.
    public void update(String title,
                       //Task task,
                       String description, LocalDateTime startTime,
                       LocalDateTime endTime, JobStatus status) {
        if (title != null && !title.equals(this.name)) {
            this.name = title;
        }
//        if (task != null && !task.equals(this.task)) {
//            this.task = task;
//        }
        if (description != null && !description.equals(this.description)) {
            this.description = description;
        }

        if (startTime != null && !startTime.equals(this.startTime)) {
            this.startTime = startTime;
        }

        if (endTime != null && !endTime.equals(this.endTime)) {
            this.endTime = endTime;
        }

        if (status != null && !status.equals(this.status)) {
            this.status = status;
        }
    }


    public void updateStatus(JobStatus status) {
        this.status = status;
        if (status == JobStatus.CANCEL || status == JobStatus.FINISHED) {
            this.endTime = LocalDateTime.now();
        } else if (status == JobStatus.REOPEN) {
            this.endTime = null;
        }
    }

    public void addAssignedUser(SubTaskAssignedUser subTaskAssignedUser) {
        this.assignedUsers.add(subTaskAssignedUser);
    }
}
