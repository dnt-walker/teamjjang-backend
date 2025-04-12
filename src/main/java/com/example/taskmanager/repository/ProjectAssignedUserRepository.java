package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.ProjectAssignedUser;
import com.example.taskmanager.domain.ProjectAssignedUserId;
import com.example.taskmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectAssignedUserRepository extends JpaRepository<ProjectAssignedUser, ProjectAssignedUserId> {

}
