package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
