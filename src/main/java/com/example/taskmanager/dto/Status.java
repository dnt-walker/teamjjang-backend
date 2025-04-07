package com.example.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {
    CREATED("CT", "created"),
    WAITING("WT", "waiting"),
    CANCEL("CC", "cancel"),
    IN_PROGRESS("IP", "in-process"),
    SUCCEEDED("SC", "succeeded"),
    FAILED("FL", "failed"),
    FINISHED("FS", "finished"),
    REMOVED("RM", "removed");

    private final String statusCode;
    private final String statusName;

    Status(String statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    @JsonValue
    public String getStatusName() {
        return this.statusName;
    }

    public static Status codeOf(String code) {
        return Arrays.stream(Status.values())
                .filter(v -> v.getStatusCode().equalsIgnoreCase(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("not found code(%s) in JobStatus.", code)));
    }

    @JsonCreator
    public static Status nameOf(String name) {
        return Arrays.stream(Status.values())
                .filter(v->v.getStatusName().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("not found value(%s) in PeJobStatusriodStatus.", name)));
    }
}
