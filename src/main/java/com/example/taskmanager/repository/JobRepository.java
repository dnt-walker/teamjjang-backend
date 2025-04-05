package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByTaskId(Long taskId);
    
    @Query("SELECT j FROM Job j WHERE j.task.id = :taskId AND j.completed = :completed")
    List<Job> findByTaskIdAndCompleted(@Param("taskId") Long taskId, @Param("completed") boolean completed);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.task.id = :taskId AND j.completed = true")
    long countCompletedJobsByTaskId(@Param("taskId") Long taskId);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.task.id = :taskId")
    long countJobsByTaskId(@Param("taskId") Long taskId);
}
