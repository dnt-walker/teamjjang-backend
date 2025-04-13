package com.example.taskmanager.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Schema(description = "상태 요약 정보 DTO")
public class StatusSummaryDto {
    private Long totalCount;
    private Long activeCount;
    private Long finishedCount;
    private Long stopCount;
    private Long cancelCount;
    private Long createdCount;
    private Long reopenedCount;
}
