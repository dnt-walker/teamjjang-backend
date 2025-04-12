package com.example.taskmanager.domain.mappedentity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ModifiedTimeEntity {

    @CreatedDate
    @Column(name = "registered_date", updatable =    false,
            columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    protected LocalDateTime registeredDate;

    @Column(name = "modified_date", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @LastModifiedDate
    protected LocalDateTime modifiedDate;
}
