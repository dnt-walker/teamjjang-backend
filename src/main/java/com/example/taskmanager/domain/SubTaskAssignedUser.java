package com.example.taskmanager.domain;

import com.example.taskmanager.domain.mappedentity.RegisteredEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "jobs_assigned_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubTaskAssignedUser extends RegisteredEntity implements Serializable {
    @EmbeddedId
    private SubTaskAssignedUserId id ;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id"
            ,foreignKey = @ForeignKey(name = "FK_assigned_subtask_user_id")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subTaskId")
    @JoinColumn(name = "subtask_id", foreignKey = @ForeignKey(name = "FK_assigned_subtask_id"))
    private SubTask subTask;

    public SubTaskAssignedUser(User user, SubTask subTask) {
       this.id = new SubTaskAssignedUserId(user.getId(), subTask.getId());
       this.user = user;
//       this.subTask = subTask;
       setSubTask(subTask);
    }

    public void setSubTask(SubTask subTask) {
        this.subTask = subTask;
        this.subTask.addAssignedUser(this);
    }

    public void removeTask() {
        this.subTask.getAssignedUsers().remove(this);
        this.subTask = null;
    }
}
