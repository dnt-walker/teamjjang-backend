package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.QProject;
import com.example.taskmanager.domain.QTask;
import com.example.taskmanager.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Project> searchProjects(Boolean active, User manager, Boolean hasTasks, Boolean upcoming) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProject project = QProject.project;
        QTask task = QTask.task;
        return null;
    }
}
