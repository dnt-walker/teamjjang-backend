package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Job;
import com.example.taskmanager.dto.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByIdAndTaskId(Long jobId, Long taskId);

    List<Job> findByTaskId(Long taskId);
    
    List<Job> findByCompletedTrue();
    
    // 기존 메서드 - 이전 버전과의 호환성을 위해 유지
    // findByAssignedUser를 JPQL 쿼리로 명시적 정의
    @Deprecated
    @Query("SELECT j FROM Job j JOIN j.assignedUsers u WHERE u = :assignedUser")
    List<Job> findByAssignedUser(@Param("assignedUser") String assignedUser);
    
    // 새로운 메서드 - assignedUsers 컬렉션에 특정 사용자가 포함된 작업 검색
    @Query("SELECT j FROM Job j JOIN j.assignedUsers u WHERE u = :username")
    List<Job> findByAssignedUsersContaining(@Param("username") String username);
    
    long countByStatus(Status status);
    
    // 기존 메서드 - 상태와 담당자로 작업 수 계산 (단일 담당자)
    @Deprecated
    @Query("SELECT COUNT(j) FROM Job j JOIN j.assignedUsers u WHERE j.status = :status AND u = :assignedUser")
    long countByStatusAndAssignedUser(@Param("status") Status status, @Param("assignedUser") String assignedUser);
    
    // 새로운 메서드 - 상태와 담당자로 작업 수 계산 (다중 담당자)
    @Query("SELECT COUNT(j) FROM Job j JOIN j.assignedUsers u WHERE j.status = :status AND u = :username")
    long countByStatusAndAssignedUsersContaining(@Param("status") Status status, @Param("username") String username);
    
    @Query("SELECT j FROM Job j WHERE j.task.id = :taskId AND j.completed = :completed")
    List<Job> findByTaskIdAndCompleted(@Param("taskId") Long taskId, @Param("completed") boolean completed);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.task.id = :taskId AND j.completed = true")
    long countCompletedJobsByTaskId(@Param("taskId") Long taskId);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.task.id = :taskId")
    long countJobsByTaskId(@Param("taskId") Long taskId);
}
