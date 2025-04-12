package com.example.taskmanager.domain;

import com.example.taskmanager.domain.mappedentity.RegisteredEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "projects_assigned_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectAssignedUser extends RegisteredEntity implements Serializable {
    @EmbeddedId
    private ProjectAssignedUserId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "FK_assigned_project_user_id")
    )
    private User user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id",
            foreignKey = @ForeignKey(name = "FK_assigned_project_id")
    )
    private Project project;

    public ProjectAssignedUser(User user, Project project) {
       this.id = new ProjectAssignedUserId(user.getId(), project.getId());
       this.user = user;
//       this.project = project;
        setProject(project);
    }

    public void setProject(Project project) {
        this.project = project;
        this.project.addAssignedUser(this);
    }

    public void removeProject() {
        this.project.getAssignedUsers().remove(this);
        this.project = null;
    }
}
