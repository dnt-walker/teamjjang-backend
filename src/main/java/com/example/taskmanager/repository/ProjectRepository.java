package com.example.taskmanager.repository;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.SubTask;
import com.example.taskmanager.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    @EntityGraph(attributePaths = {
            "assignedUsers",
            "manager",
            "register",
            "modifier"
    })
    @Query("SELECT t FROM Project t " +
            " JOIN t.assignedUsers u " +
            " JOIN t.manager a " +
            " JOIN t.register r " +
            " JOIN t.modifier m ")
//    +
//            " WHERE  ")
    List<Project> findAll();

    // Task에 속한 SubTask 목록 조회
    @EntityGraph(attributePaths = {
            "assignedUsers",
            "manager",
            "register",
            "modifier"
    })
    @Query("SELECT t FROM Project t " +
            " JOIN t.assignedUsers u " +
            " JOIN t.manager a " +
            " JOIN t.register r " +
            " JOIN t.modifier m " +
            " WHERE t.id = :projectId")
    Optional<Project> findById(@Param("projectId") Long projectId);


    @Query("SELECT count(t) FROM Project t " +
            " WHERE t.status = :status ")
    Long countByStatus(JobStatus status);

    @Query("SELECT count(t) FROM Project t " +
            " WHERE t.status not in('CC', 'FS') AND (t.endDate = null OR t.endDate < CURRENT_DATE)  ")
    Long countByOverDateProject();

}
