package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    List<Task> findByCreator(String creator);
    
    List<Task> findByAssigneesContaining(String assignee);
    
    List<Task> findByCompletionDateIsNotNull();
    
    List<Task> findByProjectId(Long projectId);
    
    @Query("SELECT t FROM Task t WHERE t.completionDate IS NULL AND t.plannedEndDate < :currentDate")
    List<Task> findOverdueTasks(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT t FROM Task t WHERE t.completionDate IS NULL")
    List<Task> findIncompleteTasks();
    
    @Query("SELECT t FROM Task t WHERE t.completionDate IS NOT NULL")
    List<Task> findCompletedTasks();
    
    @Query("SELECT t FROM Task t WHERE t.startDate <= :date AND (t.completionDate IS NULL OR t.completionDate >= :date)")
    List<Task> findActiveTasksOnDate(@Param("date") LocalDate date);
}
