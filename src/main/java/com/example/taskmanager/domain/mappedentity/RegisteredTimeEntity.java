package com.example.taskmanager.domain.mappedentity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class RegisteredTimeEntity implements Serializable {

    @CreatedDate
    @Column(name = "registered_date", updatable = false,
            columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime registeredDate;

}
