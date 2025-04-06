package com.example.taskmanager.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String statusName) {
        return Status.nameOf(statusName);
    }
}
