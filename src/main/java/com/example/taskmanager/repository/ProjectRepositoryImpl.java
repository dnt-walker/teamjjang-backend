package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.QProject;
import com.example.taskmanager.domain.QTask;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.ProjectFilterDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public List<Project> searchProjects(Boolean active, User manager, Boolean hasTasks, Boolean upcoming) {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
//        QProject project = QProject.project;
//        QTask task = QTask.task;
//        return null;
//    }
    @Override
    public List<Project> pagedProjectList(Pageable pageable, ProjectFilterDto filterDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProject project = QProject.project;
        QTask task = QTask.task;

        BooleanBuilder builder = new BooleanBuilder();

        // 상태 필터
        if (filterDto.getStatus() != null) {
            builder.and(project.status.eq(filterDto.getStatus()));
        }

        // 키워드 필터 (이름 또는 설명)
        if (StringUtils.hasText(filterDto.getKeyword())) {
            builder.and(
                project.name.containsIgnoreCase(filterDto.getKeyword())
                        .or(project.description.containsIgnoreCase(filterDto.getKeyword()))
            );
        }

        return queryFactory
                .selectFrom(project)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(project.id.desc())
                .fetch();
    }
}
