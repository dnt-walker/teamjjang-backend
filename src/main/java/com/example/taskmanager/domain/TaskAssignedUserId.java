package com.example.taskmanager.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Embeddable
public class TaskAssignedUserId implements Serializable {
    private Long userId;
    private Long taskId;

    public TaskAssignedUserId(Long userId, Long taskId) {
        this.userId = userId;
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskAssignedUserId)) return false;
        TaskAssignedUserId that = (TaskAssignedUserId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, taskId);
    }
}
