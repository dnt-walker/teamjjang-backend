package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskAssignedUser;
import com.example.taskmanager.domain.TaskAssignedUserId;
import com.example.taskmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskAssignedUserRepository extends JpaRepository<TaskAssignedUser, TaskAssignedUserId> {
    
}
