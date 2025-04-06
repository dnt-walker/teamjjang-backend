package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    Optional<Task> findByIdAndProjectId(Long taskId, Long projectId);

    List<Task> findByCreator(String creator);
    
    List<Task> findByAssigneesContaining(String assignee);
    
    List<Task> findByCompletionDateIsNotNull();
    
    List<Task> findByProjectId(Long projectId);
    
    List<Task> findByProjectIdAndCompletionDateIsNotNull(Long projectId);
    
    List<Task> findByProjectIdAndCompletionDateIsNull(Long projectId);
    
    // 카운트 메서드
    long countByAssigneesContaining(String assignee);
    
    long countByCompletionDateIsNotNull();
    
    long countByPlannedEndDateBeforeAndCompletionDateIsNull(LocalDate date);
    
    long countByPlannedEndDateBetweenAndCompletionDateIsNull(LocalDate startDate, LocalDate endDate);
    
    long countByStartDateIsNull();
    
    long countByCompletionDateIsNullAndStartDateIsNotNull();
    
    long countByCompletionDateBetween(LocalDate startDate, LocalDate endDate);
    
    long countByCompletionDateBetweenAndAssigneesContaining(LocalDate startDate, LocalDate endDate, String assignee);
    
    List<Task> findByPlannedEndDateBetweenAndCompletionDateIsNullAndAssigneesContaining(
            LocalDate startDate, LocalDate endDate, String assignee);
    
    List<Task> findByPlannedEndDateBeforeAndCompletionDateIsNullAndAssigneesContaining(
            LocalDate date, String assignee);
    
    @Query("SELECT a, COUNT(t) FROM Task t JOIN t.assignees a GROUP BY a")
    List<Object[]> countTasksByAssignee();
    
    // JobStatus에 따른 필터링을 위한 메서드들
    List<Task> findByProjectIdAndStartDateIsNull(Long projectId);
    
    List<Task> findByProjectIdAndStartDateAfter(Long projectId, LocalDate date);
    
    List<Task> findByProjectIdAndStartDateIsNotNullAndCompletionDateIsNull(Long projectId);
    
    List<Task> findByProjectIdAndPlannedEndDateBeforeAndCompletionDateIsNull(Long projectId, LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.completionDate IS NULL AND t.plannedEndDate < :currentDate")
    List<Task> findOverdueTasks(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT t FROM Task t WHERE t.completionDate IS NULL")
    List<Task> findIncompleteTasks();
    
    @Query("SELECT t FROM Task t WHERE t.completionDate IS NOT NULL")
    List<Task> findCompletedTasks();
    
    @Query("SELECT t FROM Task t WHERE t.startDate <= :date AND (t.completionDate IS NULL OR t.completionDate >= :date)")
    List<Task> findActiveTasksOnDate(@Param("date") LocalDate date);
}