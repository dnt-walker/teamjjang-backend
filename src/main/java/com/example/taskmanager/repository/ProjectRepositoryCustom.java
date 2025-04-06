package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Project;
import java.util.List;

public interface ProjectRepositoryCustom {
    
    /**
     * QueryDSL을 사용하여 다양한 조건으로 프로젝트를 검색합니다.
     * 
     * @param active 활성 상태 필터링 (true: 활성, false: 비활성)
     * @param manager 담당자 필터링
     * @param hasTasks 태스크 존재 여부 필터링 (true: 태스크 있음, false: 태스크 없음)
     * @param upcoming 예정된 프로젝트 필터링 (true: 시작일이 현재 이후인 프로젝트)
     * @return 조건에 맞는 프로젝트 목록
     */
    List<Project> searchProjects(Boolean active, String manager, Boolean hasTasks, Boolean upcoming);
}
