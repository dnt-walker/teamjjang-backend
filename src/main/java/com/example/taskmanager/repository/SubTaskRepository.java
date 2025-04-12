package com.example.taskmanager.repository;

import com.example.taskmanager.domain.SubTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {


    @EntityGraph(attributePaths = {
            "assignedUsers",
            "register",
            "modifier"
    })
    @Query("SELECT t FROM SubTask t " +
            " JOIN t.assignedUsers u " +
            " JOIN t.register r " +
            " JOIN t.modifier m " +
            " WHERE  t.task.id = :taskId ")
    List<SubTask> findByTaskId(@Param("taskId") Long taskId);
    
    // Task에 속한 SubTask 목록 조회
    @EntityGraph(attributePaths = {
            "assignedUsers",
            "register",
            "modifier"
    })
    @Query("SELECT t FROM SubTask t " +
            " JOIN t.assignedUsers u " +
            " JOIN t.register r " +
            " JOIN t.modifier m " +
            " WHERE  t.task.id = :taskId  AND t.id = :subTaskId")
    Optional<SubTask> findByTaskIdAndSubTaskId( @Param("taskId") Long taskId,
                                                @Param("subTaskId") Long subTaskId);

}
