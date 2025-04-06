package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.QProject;
import com.example.taskmanager.domain.QTask;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Project> searchProjects(Boolean active, String manager, Boolean hasTasks, Boolean upcoming) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProject project = QProject.project;
        QTask task = QTask.task;
        
        BooleanBuilder builder = new BooleanBuilder();
        
        // 활성 상태 필터링
        if (active != null) {
            builder.and(project.active.eq(active));
        }
        
        // 담당자 필터링
        if (StringUtils.hasText(manager)) {
            builder.and(project.manager.eq(manager));
        }
        
        // 예정된 프로젝트 필터링
        if (Boolean.TRUE.equals(upcoming)) {
            builder.and(project.startDate.goe(LocalDate.now()));
        }
        
        // 태스크 존재 여부 필터링
        if (hasTasks != null) {
            if (Boolean.TRUE.equals(hasTasks)) {
                return queryFactory.selectFrom(project)
                        .leftJoin(task).on(project.id.eq(task.id))
                        .where(builder)
                        .groupBy(project)
                        .having(task.count().gt(0L))
                        .fetch();
            } else {
                return queryFactory.selectFrom(project)
                        .leftJoin(task).on(project.id.eq(task.id))
                        .where(builder)
                        .groupBy(project)
                        .having(task.count().eq(0L))
                        .fetch();
            }
        }
        
        return queryFactory.selectFrom(project)
                .where(builder)
                .fetch();
    }
}
