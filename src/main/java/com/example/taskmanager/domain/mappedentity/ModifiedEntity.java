package com.example.taskmanager.domain.mappedentity;

import com.example.taskmanager.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

//@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ModifiedEntity extends ModifiedTimeEntity implements Serializable {

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id", updatable = false,
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    protected User register;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id",
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    protected User modifier;
}
