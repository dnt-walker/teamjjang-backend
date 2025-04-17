package com.example.taskmanager.domain;

import com.example.taskmanager.domain.mappedentity.ModifiedEntity;
import com.example.taskmanager.constant.JobStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Project 엔티티는 프로젝트의 기본 정보를 관리합니다.
 * ModifiedEntity를 상속받아 생성자/수정자 및 시간 정보를 자동으로 처리합니다.
 * 주요 필드:
 *   - id: 프로젝트의 고유 식별자 (자동 생성).
 *   - name: 프로젝트 이름 (최대 128자).
 *   - description: 프로젝트 설명 (TEXT 타입).
 *   - startDate, endDate: 프로젝트 시작 및 종료 날짜.
 *   - manager: 프로젝트 관리자 (User 엔티티와 다대일 관계, 지연 로딩).
 *   - complete: 프로젝트 완료 여부.
 *   - active: 프로젝트 활성화 상태.
 */
@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends ModifiedEntity implements Serializable {
    // 기본 키, 자동 생성 (IDENTITY 전략 사용)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    // 프로젝트 이름 (최대 128자)
    @Column(name = "project_name", length = 128)
    private String name;

    // 프로젝트 상세 설명 (TEXT 타입)
    @Column(name = "project_desc", columnDefinition = "TEXT")
    private String description;

    // 프로젝트 시작 날짜
    @Column(name = "start_date")
    private LocalDate startDate;

    // 프로젝트 종료 날짜
    @Column(name = "end_date")
    private LocalDate endDate;

    // 프로젝트 관리자 (User 엔티티와의 다대일 관계, LAZY 로딩)
    // @JsonIgnore로 직렬화 시 제외하여 무한 재귀 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id",
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    @JsonIgnore
    private User manager;

    @Column(name = "project_status", length = 2)
    @Convert(converter = JobStatusConverter.class)
    private JobStatus status;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectAssignedUser> assignedUsers = new ArrayList<>();



    public Project(Long id, String name, String description,
                   LocalDate startDate,
                   LocalDate endDate, User manager) {
        if (id != null) {
            this.id = id;
        }
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.manager = manager;
        this.status = JobStatus.CREATED;
    }

    // 업데이트 메서드: 현재 Project 엔티티의 멤버 값을 업데이트합니다. 각 파라미터의 값이 null이 아니고 기존 값과 다를 경우 업데이트합니다.
    public void update(String name, String description,
                       LocalDate startDate,
                       LocalDate endDate,
                       User manager,
                       JobStatus status) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
        }
        if (description != null && !description.equals(this.description)) {
            this.description = description;
        }
        if (startDate != null && !startDate.equals(this.startDate)) {
            this.startDate = startDate;
        }
        if (endDate != null && !endDate.equals(this.endDate)) {
            this.endDate = endDate;
        }
        if (manager != null && !manager.equals(this.manager)) {
            this.manager = manager;
        }
        if (status!= null) {
            this.status = status;
        }
    }

    public void addAssignedUser(ProjectAssignedUser assignedUser) {
        assignedUsers.add(assignedUser);
        assignedUser.setProject(this); // 역방향 연결도 동기화
    }

    public void removeAssignedUser(ProjectAssignedUser assignedUser) {
        assignedUser.setProject(null);
        assignedUsers.remove(assignedUser);
    }

    public void clearAssignedUsers() {
        assignedUsers.clear();
    }
}
