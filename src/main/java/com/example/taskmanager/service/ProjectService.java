package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.ProjectAssignedUser;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.dto.ProjectFilterDto;
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
                .map(ProjectDto::from)
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
    public ProjectDto createProjectWithUsers(ProjectDto projectDto) {
        Project project = projectDto.toEntity(userService::getUserById);

        List<ProjectAssignedUser> assignedUsers = projectDto.getAssignees().stream()
                .map(userDto -> {
                    User user = userRepository.findById(userDto.getId())
                            .orElseThrow(() -> new NoSuchElementException("User not found: " + userDto.getId()));
                    return new ProjectAssignedUser(user, project);
                }).collect(Collectors.toList());

//        project.setAssignedUsers(assignedUsers);

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
    public Project updateProjectWithUsers(Long projectId, ProjectDto projectDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));


        project.update(projectDto.getName(), projectDto.getDescription(),
                projectDto.getStartDate(), projectDto.getEndDate(),
                userService.getUserById(projectDto.getManager().getId()),
                projectDto.getStatus());

        project.getAssignedUsers().forEach(ProjectAssignedUser::removeProject);

        List<ProjectAssignedUser> updatedUsers = projectDto.getAssignees().stream()
                .map(userDto -> {
                    User user = userRepository.findById(userDto.getId())
                            .orElseThrow(() -> new NoSuchElementException("User not found: " + userDto.getId()));
                    return new ProjectAssignedUser(user, project);
                }).collect(Collectors.toList());

//        project.getAssignedUsers().addAll(updatedUsers);

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));
        projectRepository.delete(project);
    }
}
