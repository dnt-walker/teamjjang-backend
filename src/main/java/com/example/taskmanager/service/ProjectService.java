package com.example.taskmanager.service;

import com.example.taskmanager.constant.JobStatus;
import com.example.taskmanager.domain.Project;
import com.example.taskmanager.domain.ProjectAssignedUser;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.ProjectDto;
import com.example.taskmanager.repository.ProjectAssignedUserRepository;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final ProjectAssignedUserRepository projectAssignedUserRepository;
    private final UserService userService;

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
    public List<ProjectDto> getProjectList() {
        return projectRepository.findAll().stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
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
