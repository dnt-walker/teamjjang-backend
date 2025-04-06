package com.example.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum JobStatus {
    CREATED("C", "created"),
    WAITING("W", "waiting"),
    // todo
    IN_PROGRESS("P", "in-process"),
    SUCCEEDED("S", "succeeded"),
    FAILED("F", "failed"),
    FINISHED("E", "finished"),
    REMOVED("R", "removed");

    private final String statusCode;
    private final String statusName;

    JobStatus(String statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    @JsonValue
    public String getStatusName() {
        return this.statusName;
    }

    public static JobStatus codeOf(String code) {
        return Arrays.stream(JobStatus.values())
                .filter(v -> v.getStatusCode().equalsIgnoreCase(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("not found code(%s) in JobStatus.", code)));
    }

    @JsonCreator
    public static JobStatus nameOf(String name) {
        return Arrays.stream(JobStatus.values())
                .filter(v->v.getStatusName().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("not found value(%s) in PeJobStatusriodStatus.", name)));
    }
}
