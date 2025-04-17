package com.example.taskmanager.domain;

import com.example.taskmanager.domain.mappedentity.ModifiedEntity;
import com.example.taskmanager.constant.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Task 엔티티는 태스크의 기본 정보를 관리합니다.
 * ModifiedEntity를 상속받아 생성자/수정자 및 시간 정보를 자동으로 처리합니다.
 * 주요 필드:
 *   - id: 태스크의 고유 식별자 (자동 생성).
 *   - project: 태스크가 속한 프로젝트 (Project 엔티티와 다대일 관계, LAZY 로딩).
 *   - name: 태스크 이름 (최대 128자).
 *   - startDate: 태스크 시작 날짜.
 *   - plannedEndDate: 계획된 종료 날짜.
 *   - completionDate: 실제 종료 날짜.
 *   - description: 태스크 상세 설명 (TEXT 타입).
 *   - status: 태스크 상태 (Status 열거형, 변환기 사용).
 */
@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 기본 키, 자동 생성 (IDENTITY 전략 사용)
public class Task extends ModifiedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    // 태스크가 속한 프로젝트 (Project 엔티티와의 다대일 관계, LAZY 로딩)
    // @JsonIgnore로 직렬화 시 제외하여 무한 재귀 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "project_id",
            foreignKey = @ForeignKey(name = "FK_task_project_id")
//            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    private Project project;

    // 태스크 이름 (최대 128자)
    @Column(name = "task_name", length = 128)
    private String name;

    // 태스크 시작 날짜
    @Column(name = "start_date")
    private LocalDate startDate;

    // 계획된 종료 날짜
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;

    // 실제 종료 날짜
    @Column(name = "completion_date")
    private LocalDate completionDate;

    // 태스크 상세 설명 (TEXT 타입)
    @Lob
    @Column(name = "task_desc", columnDefinition = "TEXT")
    private String description;

    // 태스크 상태 (Status 열거형, 변환기 사용)
    @Column(name = "task_status", length = 2)
    @Convert(converter = JobStatusConverter.class)
    private JobStatus status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAssignedUser> assignedUsers = new ArrayList<>();

    // 생성자: 모든 필드 초기화 (id 포함)
    public Task(Long id, Project project, String name,
                LocalDate startDate, LocalDate plannedEndDate, LocalDate completionDate,
                String description, JobStatus status) {
        if (id != null) {
            this.id = id;
        }
        this.project = project;
        this.name = name;
        this.startDate = startDate;
        this.plannedEndDate = plannedEndDate;
        this.completionDate = completionDate;
        this.description = description;
        this.status = status;
    }

    // 업데이트 메서드: 현재 Task 엔티티의 멤버 값을 업데이트합니다. 각 파라미터의 값이 null이 아니고 기존 값과 다를 경우 업데이트합니다.
    public void update(String name,
                       LocalDate startDate, LocalDate plannedEndDate, LocalDate completionDate,
                       String description, JobStatus status) {
        if (project != null && !project.equals(this.project)) {
            this.project = project;
        }
        if (name != null && !name.equals(this.name)) {
            this.name = name;
        }
        if (startDate != null && !startDate.equals(this.startDate)) {
            this.startDate = startDate;
        }
        if (plannedEndDate != null && !plannedEndDate.equals(this.plannedEndDate)) {
            this.plannedEndDate = plannedEndDate;
        }
        if (completionDate != null && !completionDate.equals(this.completionDate)) {
            this.completionDate = completionDate;
        }
        if (description != null && !description.equals(this.description)) {
            this.description = description;
        }
        if (status != null && !status.equals(this.status)) {
            this.status = status;
        }
    }


    public void updateStatus(JobStatus status) {
        this.status = status;
        if (status == JobStatus.CANCEL || status == JobStatus.FINISHED) {
            this.completionDate = LocalDate.now();
        } else if (status == JobStatus.REOPEN) {
            this.completionDate = null;
        }
    }

    public void addAssignedUser(TaskAssignedUser assignedUser) {
        assignedUsers.add(assignedUser);
        assignedUser.setTask(this); // 역방향 연결도 동기화
    }

    public void removeAssignedUser(TaskAssignedUser assignedUser) {
        assignedUser.setTask(null);
        assignedUsers.remove(assignedUser);
    }

    public void clearAssignedUsers() {
        assignedUsers.clear();
    }
}
