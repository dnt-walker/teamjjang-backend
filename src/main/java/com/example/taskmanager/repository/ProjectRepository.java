package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    // 활성화된 프로젝트만 조회
    List<Project> findByActiveTrue();
    
    // 특정 담당자의 프로젝트 조회
    List<Project> findByManager(String manager);
    
    // 커스텀 쿼리: 특정 날짜 이후에 시작하는 프로젝트 조회
    @Query("SELECT p FROM Project p WHERE p.startDate >= CURRENT_DATE")
    List<Project> findUpcomingProjects();
}
