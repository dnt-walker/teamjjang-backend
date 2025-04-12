package com.example.taskmanager.domain;

import com.example.taskmanager.constant.JobStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class TaskStatusConverter implements AttributeConverter<JobStatus, String> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TaskStatusConverter.class);

    public TaskStatusConverter() {
    }

    public String convertToDatabaseColumn(JobStatus status) {
        return status == null ? null : status.getStatusCode();
    }

    public JobStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        } else {
            JobStatus result = null;

            try {
                result = JobStatus.codeOf(code);
            } catch (IllegalArgumentException aex) {
                log.error("## failure to convert cause unexperted name [{}]", aex);
            }

            return result;
        }
    }
}