package com.example.taskmanager.repository;

import com.example.taskmanager.domain.SubTask;
import com.example.taskmanager.domain.SubTaskAssignedUser;
import com.example.taskmanager.domain.SubTaskAssignedUserId;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.constant.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SubTaskAssignedUserRepository extends JpaRepository<SubTaskAssignedUser, SubTaskAssignedUserId> {

  @Query("SELECT st FROM SubTaskAssignedUser st" +
          " LEFT JOIN FETCH st.user " +
          " WHERE st.subTask.task.id = :taskId")
  List<SubTask> findSubTasksWithAssignedUsersByTaskId(@Param("taskId") Long taskId);
}
