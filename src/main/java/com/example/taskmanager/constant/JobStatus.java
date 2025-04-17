package com.example.taskmanager.constant;
import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Schema(description = "작업 상태 코드")
public enum JobStatus {

    @Schema(description = "생성됨")
    CREATED("CT", "created"),
    @Schema(description = "진행중")
    IN_PROGRESS("IP", "in-process"),
    @Schema(description = "중단됨")
    STOP("ST", "stop"),
    @Schema(description = "취소됨")
    CANCEL("CC", "cancel"),
    @Schema(description = "완료됨")
    FINISHED("FS", "finished"),
    @Schema(description = "재오픈")
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
