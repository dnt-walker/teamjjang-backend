package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskAssignedUser;
import com.example.taskmanager.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> searchTasks(String keyword, String creatorUsername, String assigneeUsername,
                                 Boolean isCompleted, LocalDate startDateFrom, LocalDate startDateTo,
                                 LocalDate endDateFrom, LocalDate endDateTo, Long projectId) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);
        
        // 필요한 Join 설정
        Join<Task, User> registerJoin = null;
        
        List<Predicate> predicates = new ArrayList<>();
        
        // 프로젝트 ID로 필터링
        if (projectId != null) {
            predicates.add(cb.equal(task.get("project").get("id"), projectId));
        }
        
        // 키워드 검색 (이름 또는 설명에서)
        if (StringUtils.hasText(keyword)) {
            Predicate namePredicate = cb.like(cb.lower(task.get("name")), "%" + keyword.toLowerCase() + "%");
            Predicate descriptionPredicate = cb.like(cb.lower(task.get("description")), "%" + keyword.toLowerCase() + "%");
            predicates.add(cb.or(namePredicate, descriptionPredicate));
        }
        
        // 생성자 사용자명으로 필터링
        if (StringUtils.hasText(creatorUsername)) {
            if (registerJoin == null) {
                registerJoin = task.join("register", JoinType.INNER);
            }
            predicates.add(cb.equal(registerJoin.get("username"), creatorUsername));
        }
        
        // 할당자 사용자명으로 필터링 - TaskAssignedUser 활용
        if (StringUtils.hasText(assigneeUsername)) {
            // 서브쿼리 사용
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<TaskAssignedUser> taskAssignedUserRoot = subquery.from(TaskAssignedUser.class);
            Join<TaskAssignedUser, User> userJoin = taskAssignedUserRoot.join("user", JoinType.INNER);
            
            subquery.select(taskAssignedUserRoot.get("task").get("id"))
                    .where(cb.and(
                            cb.equal(taskAssignedUserRoot.get("task").get("id"), task.get("id")),
                            cb.equal(userJoin.get("username"), assigneeUsername)
                    ));
            
            predicates.add(cb.exists(subquery));
        }
        
        // 완료 상태로 필터링
        if (isCompleted != null) {
            if (isCompleted) {
                predicates.add(cb.isNotNull(task.get("completionDate")));
            } else {
                predicates.add(cb.isNull(task.get("completionDate")));
            }
        }
        
        // 시작일 범위로 필터링
        if (startDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("startDate"), startDateFrom));
        }
        
        if (startDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("startDate"), startDateTo));
        }
        
        // 종료 예정일 범위로 필터링
        if (endDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("plannedEndDate"), endDateFrom));
        }
        
        if (endDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("plannedEndDate"), endDateTo));
        }
        
        // 조건 적용
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // 정렬 (시작일 오름차순)
        cq.orderBy(cb.asc(task.get("startDate")));
        
        // 쿼리 실행 및 결과 반환
        TypedQuery<Task> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Task> findTasksByUserInvolved(String username, Boolean isCompleted) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);
        
        Join<Task, User> registerJoin = task.join("register", JoinType.LEFT);
        
        // 할당된 태스크를 찾기 위한 서브쿼리
        Subquery<Long> assignedTasksSubquery = cq.subquery(Long.class);
        Root<TaskAssignedUser> taskAssignedUserRoot = assignedTasksSubquery.from(TaskAssignedUser.class);
        Join<TaskAssignedUser, User> userJoin = taskAssignedUserRoot.join("user", JoinType.INNER);
        
        assignedTasksSubquery.select(taskAssignedUserRoot.get("task").get("id"))
                .where(cb.equal(userJoin.get("username"), username));
        
        // 사용자가 생성자이거나 할당자인 태스크 조회
        Predicate creatorPredicate = cb.equal(registerJoin.get("username"), username);
        Predicate assigneePredicate = cb.in(task.get("id")).value(assignedTasksSubquery);
        Predicate userInvolvedPredicate = cb.or(creatorPredicate, assigneePredicate);
        
        // 완료 상태 필터링
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(userInvolvedPredicate);
        
        if (isCompleted != null) {
            if (isCompleted) {
                predicates.add(cb.isNotNull(task.get("completionDate")));
            } else {
                predicates.add(cb.isNull(task.get("completionDate")));
            }
        }
        
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(task.get("plannedEndDate")));
        
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Task> findHighPriorityTasks(int daysThreshold, String assigneeUsername) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);
        
        // 기본 조건: 미완료 + 마감일 임박
        Predicate incompletePredicate = cb.isNull(task.get("completionDate"));
        Predicate dueDatePredicate = cb.lessThanOrEqualTo(task.get("plannedEndDate"), thresholdDate);
        Predicate hasStartDatePredicate = cb.isNotNull(task.get("startDate"));
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(incompletePredicate);
        predicates.add(dueDatePredicate);
        predicates.add(hasStartDatePredicate);
        
        // 할당자 필터링 (선택적)
        if (StringUtils.hasText(assigneeUsername)) {
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<TaskAssignedUser> taskAssignedUserRoot = subquery.from(TaskAssignedUser.class);
            Join<TaskAssignedUser, User> userJoin = taskAssignedUserRoot.join("user", JoinType.INNER);
            
            subquery.select(taskAssignedUserRoot.get("task").get("id"))
                    .where(cb.and(
                            cb.equal(taskAssignedUserRoot.get("task").get("id"), task.get("id")),
                            cb.equal(userJoin.get("username"), assigneeUsername)
                    ));
            
            predicates.add(cb.exists(subquery));
        }
        
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        
        // 마감일이 빠른 순서로 정렬
        cq.orderBy(cb.asc(task.get("plannedEndDate")));
        
        return entityManager.createQuery(cq).getResultList();
    }
}