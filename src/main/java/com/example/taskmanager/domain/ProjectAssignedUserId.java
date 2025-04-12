package com.example.taskmanager.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Embeddable
public class ProjectAssignedUserId implements Serializable {
    private Long userId;
    private Long projectId;

    public ProjectAssignedUserId(Long userId, Long projectId) {
        this.userId = userId;
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectAssignedUserId)) return false;
        ProjectAssignedUserId that = (ProjectAssignedUserId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, projectId);
    }
}
