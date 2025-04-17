package com.example.taskmanager.domain;

import com.example.taskmanager.domain.mappedentity.RegisteredEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "tasks_assigned_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskAssignedUser extends RegisteredEntity implements Serializable {
    @EmbeddedId
    private TaskAssignedUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "FK_assigned_task_user_id")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id",
            foreignKey = @ForeignKey(name = "FK_assigned_task_id")
    )
    private Task task;


    public TaskAssignedUser(User user, Task task) {
       this.id = new TaskAssignedUserId(user.getId(), task.getId());
       this.user = user;
//       this.task = task;
       setTask(task);
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
