package com.example.taskmanager.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToJobStatusConverter implements Converter<String, JobStatus> {
    @Override
    public JobStatus convert(String statusName) {
        return JobStatus.nameOf(statusName);
    }
}
