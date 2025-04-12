package com.example.taskmanager.repository;

import com.example.taskmanager.domain.SubTask;
import com.example.taskmanager.domain.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    @EntityGraph(attributePaths = {
            "project",
            "register",
            "modifier"
    })
    @Query("SELECT t FROM Task t " +
            " JOIN t.assignedUsers u " +
            " JOIN t.register r " +
            " JOIN t.modifier m " +
            " WHERE  t.project.id = :projectId ")
    List<Task> findByProjectId(@Param("projectId") Long projectId);

    @EntityGraph(attributePaths = {
            "project",
            "register",
            "modifier"
    })
    @Query("SELECT t FROM Task t " +
            " JOIN t.project p " +
            " JOIN t.register r " +
            " JOIN t.modifier m " +
            " WHERE t.id = :taskId AND p.id = :projectId")
    Optional<Task> findByProjectIdAndId( @Param("projectId") Long projectId, @Param("taskId") Long taskId);

}