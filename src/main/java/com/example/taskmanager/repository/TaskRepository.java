package com.example.taskmanager.repository;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    @Query("SELECT t FROM Task t " +
            " JOIN t.assignedUsers u " +
            " JOIN t.register r " +
            " JOIN t.modifier m ")
    Page<Task> findAll(Pageable pageable);

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

    Long countByStatus(JobStatus status);


    @Query("SELECT count(t) FROM Task t " +
            " JOIN t.project p " +
            " WHERE p.id = :projectId")
    Long countByProject(@Param("projectId") Long projectId);

    @Query("SELECT count(t) FROM Task t " +
            " JOIN t.project p " +
            " WHERE p.id = :projectId AND p.status = :status")
    Long countByProjectAndStatus(@Param("projectId") Long projectId,
                        @Param("status") JobStatus status);


    @Query("SELECT count(t) FROM Task t " +
            " JOIN t.project p " +
            " JOIN t.assignedUsers a " +
            " WHERE p.id = :projectId AND a.user = :user ")
    Long countByProjectAndUser(@Param("projectId") Long projectId, @Param("user") User user);

    @Query("SELECT count(t) FROM Task t " +
            " JOIN t.project p " +
            " JOIN t.assignedUsers a " +
            " WHERE p.id = :projectId AND p.status = :status AND a.user = :user ")
    Long countByProjectAndStatusAndUser(@Param("projectId") Long projectId,
                                 @Param("status") JobStatus status,
                                  @Param("user") User user);


    @Query("SELECT count(t) FROM Task t " +
            " WHERE  t.status not in('CC', 'FS') AND (t.completionDate = null OR t.completionDate < CURRENT_DATE)  ")
    Long countByAllOverDateTask();

    @Query("SELECT count(t) FROM Task t " +
            " JOIN t.project p " +
            " WHERE p.id = :projectId " +
            " AND t.status not in('CC', 'FS') AND (t.completionDate = null OR t.completionDate < CURRENT_DATE)  ")
    Long countByProjectOverDateTask(@Param("projectId") Long projectId);

    @Query("SELECT count(t) FROM Task t " +
            " JOIN t.project p " +
            " WHERE p.id = :projectId " +
            " AND t.status not in('CC', 'FS') AND (t.completionDate = null OR t.completionDate < CURRENT_DATE) " +
            " AND  :user member of t.assignedUsers ")
    Long countByProjectAndAssignedUsersOverDateTask(@Param("projectId") Long projectId, @Param("user") User user);


}