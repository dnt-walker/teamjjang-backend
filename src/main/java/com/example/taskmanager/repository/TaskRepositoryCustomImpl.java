package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> searchTasks(String keyword, String creator, String assignee,
                                 Boolean isCompleted, LocalDate startDateFrom,
                                 LocalDate startDateTo, LocalDate endDateFrom,
                                 LocalDate endDateTo) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Search by keyword in name or description
        if (StringUtils.hasText(keyword)) {
            Predicate namePredicate = cb.like(cb.lower(task.get("name")), "%" + keyword.toLowerCase() + "%");
            Predicate descriptionPredicate = cb.like(cb.lower(task.get("description")), "%" + keyword.toLowerCase() + "%");
            predicates.add(cb.or(namePredicate, descriptionPredicate));
        }
        
        // Filter by creator
        if (StringUtils.hasText(creator)) {
            predicates.add(cb.equal(task.get("creator"), creator));
        }
        
        // Filter by assignee
        if (StringUtils.hasText(assignee)) {
            predicates.add(cb.isMember(assignee, task.get("assignees")));
        }
        
        // Filter by completion status
        if (isCompleted != null) {
            if (isCompleted) {
                predicates.add(cb.isNotNull(task.get("completionDate")));
            } else {
                predicates.add(cb.isNull(task.get("completionDate")));
            }
        }
        
        // Filter by start date range
        if (startDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("startDate"), startDateFrom));
        }
        
        if (startDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("startDate"), startDateTo));
        }
        
        // Filter by end date range
        if (endDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("plannedEndDate"), endDateFrom));
        }
        
        if (endDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("plannedEndDate"), endDateTo));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        cq.orderBy(cb.asc(task.get("startDate")));
        
        TypedQuery<Task> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
