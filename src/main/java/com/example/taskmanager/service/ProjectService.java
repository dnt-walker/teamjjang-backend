package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.ProjectAssignedUser;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.dto.ProjectFilterDto;
import com.example.taskmanager.dto.ProjectRequestDto;
import com.example.taskmanager.dto.StatusSummaryDto;
import com.example.taskmanager.repository.ProjectAssignedUserRepository;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectAssignedUserRepository projectAssignedUserRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectList(Pageable pageable,
                                           ProjectFilterDto filterDto) {

        if (pageable == null) {
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "startDate"));
        }
        return projectRepository.pagedProjectList(pageable, filterDto).stream()
                .map(entity->{
                    ProjectDto dto = ProjectDto.from(entity);
                    Long totalCount = taskRepository.countByProject(entity.getId());
                    Long completedTaskCount = taskRepository.countByProjectCompltedTask(entity.getId());
                    if (totalCount != 0) {
                        dto.setCompletedTasksPercentage((int) ((completedTaskCount / totalCount) * 100));
                    } else {
                        dto.setCompletedTasksPercentage(0);
                    }
                    dto.setTaskStatusSummary(getTaskSummary(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public StatusSummaryDto getProjectSummary() {
        long totalProjectCount = projectRepository.count();
        long activeProjectCount = projectRepository.countByStatus(JobStatus.IN_PROGRESS);
        long finishedProjectCount = projectRepository.countByStatus(JobStatus.FINISHED);
        long stoppedProjectCount = projectRepository.countByStatus(JobStatus.STOP);
        long cancelProjectCount = projectRepository.countByStatus(JobStatus.CANCEL);
        long repoenProjectCount = projectRepository.countByStatus(JobStatus.REOPEN);
        long createProjectCount = projectRepository.countByStatus(JobStatus.CREATED);
        long overDateProjectCount = projectRepository.countByOverDateProject();
        StatusSummaryDto dto = new StatusSummaryDto(totalProjectCount, activeProjectCount,
                finishedProjectCount, stoppedProjectCount,
                cancelProjectCount,createProjectCount, repoenProjectCount,
                overDateProjectCount);

        return dto;
    }


    public StatusSummaryDto getTaskSummary(Long projectId) {
        long totalProjectCount = taskRepository.countByProject(projectId);
        long activeProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.IN_PROGRESS);
        long finishedProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.FINISHED);
        long stoppedProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.STOP);
        long cancelProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.CANCEL);
        long repoenProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.REOPEN);
        long createProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.CREATED);
        long overDateTaskCount = taskRepository.countByProjectOverDateTask(projectId);

        StatusSummaryDto dto = new StatusSummaryDto(totalProjectCount, activeProjectCount,
                finishedProjectCount, stoppedProjectCount,
                cancelProjectCount,createProjectCount, repoenProjectCount,
                overDateTaskCount);

        return dto;
    }

//    public StatusSummaryDto getUserTaskSummary(UserDetails userDetails, Long projectId) {
//        TeamUserDetails teamUserDetails = (TeamUserDetails)userDetails;
//        long totalProjectCount = taskRepository.countByProject(projectId);
//        long activeProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.IN_PROGRESS, );
//        long finishedProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.FINISHED);
//        long stoppedProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.STOP);
//        long cancelProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.CANCEL);
//        long repoenProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.REOPEN);
//        long createProjectCount = taskRepository.countByProjectAndStatus(projectId, JobStatus.CREATED);
//        long overDateTaskCount = taskRepository.countByOverDateTask(projectId);
//        StatusSummaryDto dto = new StatusSummaryDto(totalProjectCount, activeProjectCount,
//                finishedProjectCount, stoppedProjectCount, cancelProjectCount,createProjectCount, repoenProjectCount);
//
//        return dto;
//    }
//


    @Transactional
    public ProjectDto createProjectWithUsers(ProjectRequestDto projectDto) {

        Project project = projectDto.toEntity(userService::getUserById);

        // 연관 사용자 등록 (양방향 연관관계 설정)
        projectDto.getAssignees().forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
            ProjectAssignedUser assignedUser = new ProjectAssignedUser(user, project);
            project.addAssignedUser(assignedUser); // 양방향 연관관계 설정
        });

        Project savedProject = projectRepository.save(project);

        return ProjectDto.from(savedProject);
    }

    @Transactional(readOnly = true)
    public ProjectDto getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));
        return ProjectDto.from(project);
    }

    @Transactional
    public Project updateProjectWithUsers(Long projectId, ProjectRequestDto projectDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));

        // 기존 연관관계 제거: 양방향 관계를 정확히 끊어줘야 Hibernate 오류 방지 가능
        List<ProjectAssignedUser> existingUsers = new ArrayList<>(project.getAssignedUsers());
        project.getAssignedUsers().clear(); // 컬렉션에서 제거
        projectAssignedUserRepository.deleteAllInBatch(existingUsers); // 강제 삭제

        // 새로운 연관관계 설정
        projectDto.getAssignees().forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
            ProjectAssignedUser assignedUser = new ProjectAssignedUser(user, project);
            project.addAssignedUser(assignedUser); // 양방향 연관관계 설정
        });

        project.update(projectDto.getName(), projectDto.getDescription(),
                projectDto.getStartDate(), projectDto.getEndDate(),
                userService.getUserById(projectDto.getManagerId()),
                projectDto.getStatus());

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));

        // 연관된 ProjectAssignedUser 제거 (orphanRemoval로 인해 DB에서도 제거됨)
        project.getAssignedUsers().clear();

        // 만약 Task도 cascade 설정되어 있지 않다면 명시적 삭제 필요
        // taskRepository.deleteByProjectId(projectId);

        // 최종 프로젝트 삭제
        projectRepository.delete(project);
    }
}
