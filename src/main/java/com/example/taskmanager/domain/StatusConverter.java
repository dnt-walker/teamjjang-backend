package com.example.taskmanager.domain;

import com.example.taskmanager.dto.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class StatusConverter implements AttributeConverter<Status, String> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(StatusConverter.class);

    public StatusConverter() {
    }

    public String convertToDatabaseColumn(Status status) {
        return status == null ? null : status.getStatusCode();
    }

    public Status convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        } else {
            Status result = null;

            try {
                result = Status.codeOf(code);
            } catch (IllegalArgumentException aex) {
                log.error("## failure to convert cause unexperted name [{}]", aex);
            }

            return result;
        }
    }
}