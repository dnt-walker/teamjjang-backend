package com.example.taskmanager.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Schema(description = "상태 요약 정보 DTO")
/**
 * Task 상태별 개수 요약 정보를 담는 DTO 클래스입니다.
 * - totalCount: 전체 Project/Task/SubTask 수
 * - activeCount: 진행중인 Project/Task/SubTask 수
 * - finishedCount: 완료된 Project/Task/SubTask 수
 * - stopCount: 중단된 Project/Task/SubTask 수
 * - cancelCount: 취소된 Project/Task/SubTask 수
 * - createdCount: 새로 생성된 Project/Task/SubTask 수
 * - reopenedCount: 재개된 Project/Task/SubTask 수
 */
public class StatusSummaryDto {
    @Schema(description = "전체 Project/Task/SubTask 수")
    private Long totalCount;
    @Schema(description = "진행중인 Project/Task/SubTask 수")
    private Long inProcessCount;
    @Schema(description = "완료된 Project/Task/SubTask 수")
    private Long finishedCount;
    @Schema(description = "중단된 Project/Task/SubTask 수")
    private Long stopCount;
    @Schema(description = "취소된 Project/Task/SubTask 수")
    private Long cancelCount;
    @Schema(description = "새로 생성된 Project/Task/SubTask 수")
    private Long createdCount;
    @Schema(description = "재개된 Project/Task/SubTask 수")
    private Long reopenedCount;
    private Long overDateCount;
}
