package com.example.taskmanager.repository;

import com.example.taskmanager.domain.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepositoryCustom {
    
    /**
     * 복합 조건을 사용하여 태스크를 검색합니다.
     * 
     * @param keyword 이름 또는 설명에 포함된 키워드
     * @param creatorUsername 태스크 생성자의 사용자명
     * @param assigneeUsername 태스크 할당자의 사용자명
     * @param isCompleted 완료 여부
     * @param startDateFrom 시작일 범위(시작)
     * @param startDateTo 시작일 범위(종료)
     * @param endDateFrom 종료 예정일 범위(시작)
     * @param endDateTo 종료 예정일 범위(종료)
     * @param projectId 프로젝트 ID
     * @return 검색 조건에 맞는 태스크 목록
     */
    List<Task> searchTasks(String keyword, String creatorUsername, String assigneeUsername,
                          Boolean isCompleted, LocalDate startDateFrom, LocalDate startDateTo, 
                          LocalDate endDateFrom, LocalDate endDateTo, Long projectId);
    
    /**
     * 사용자가 관련된 태스크를 검색합니다(생성자 또는 할당자).
     * 
     * @param username 사용자명
     * @param isCompleted 완료 여부
     * @return 사용자와 관련된 태스크 목록
     */
    List<Task> findTasksByUserInvolved(String username, Boolean isCompleted);
    
    /**
     * 우선순위가 높은 태스크를 검색합니다(마감일 임박, 미완료).
     * 
     * @param daysThreshold 마감일까지 남은 일수 기준
     * @param assigneeUsername 특정 담당자로 필터링(선택사항)
     * @return 우선순위가 높은 태스크 목록
     */
    List<Task> findHighPriorityTasks(int daysThreshold, String assigneeUsername);
}