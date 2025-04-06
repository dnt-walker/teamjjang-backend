package com.example.taskmanager.domain;

import com.example.taskmanager.dto.JobStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class JobStatusConverter implements AttributeConverter<JobStatus, String> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(JobStatusConverter.class);

    public JobStatusConverter() {
    }

    public String convertToDatabaseColumn(JobStatus jobStatus) {
        return jobStatus == null ? null : jobStatus.getStatusCode();
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