package com.example.taskmanager.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Embeddable
public class SubTaskAssignedUserId implements Serializable {
    private Long userId;
    private Long subTaskId;

    public SubTaskAssignedUserId(Long userId, Long subTaskId) {
        this.userId = userId;
        this.subTaskId = subTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTaskAssignedUserId)) return false;
        SubTaskAssignedUserId that = (SubTaskAssignedUserId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(subTaskId, that.subTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, subTaskId);
    }
}
