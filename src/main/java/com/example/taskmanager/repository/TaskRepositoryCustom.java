package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepositoryCustom {
    
    List<Task> searchTasks(String keyword, String creator, String assignee, 
                          Boolean isCompleted, LocalDate startDateFrom, 
                          LocalDate startDateTo, LocalDate endDateFrom, 
                          LocalDate endDateTo);
}
