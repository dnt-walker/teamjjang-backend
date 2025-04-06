package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.dto.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    // 기존 코드 아래에 추가
    Optional<Job> findByIdAndTaskId(Long jobId, Long taskId);

    List<Job> findByTaskId(Long taskId);
    
    List<Job> findByCompletedTrue();
    
    List<Job> findByAssignedUser(String assignedUser);
    
    long countByStatus(Status status);
    
    long countByStatusAndAssignedUser(Status status, String assignedUser);
    
    @Query("SELECT j FROM Job j WHERE j.task.id = :taskId AND j.completed = :completed")
    List<Job> findByTaskIdAndCompleted(@Param("taskId") Long taskId, @Param("completed") boolean completed);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.task.id = :taskId AND j.completed = true")
    long countCompletedJobsByTaskId(@Param("taskId") Long taskId);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.task.id = :taskId")
    long countJobsByTaskId(@Param("taskId") Long taskId);
}
