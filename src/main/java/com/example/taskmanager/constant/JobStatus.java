package com.example.taskmanager.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum JobStatus {
    CREATED("CT", "created"),
    IN_PROGRESS("IP", "in-process"),
    STOP("ST", "stop"),
    CANCEL("CC", "cancel"),
    FINISHED("FS", "finished"),
    REOPEN("RO", "reopen");

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
                .orElseThrow(() -> new IllegalArgumentException(String.format("not found code(%s) in WorkStatus.", code)));
    }

    @JsonCreator
    public static JobStatus nameOf(String name) {
        return Arrays.stream(JobStatus.values())
                .filter(v->v.getStatusName().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("not found value(%s) in WorkStatus.", name)));
    }
}
