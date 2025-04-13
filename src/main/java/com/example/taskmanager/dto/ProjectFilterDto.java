package com.example.taskmanager.dto;

import com.example.taskmanager.constant.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "캠페인 필터링")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFilterDto {

//    @Schema(description = "에셋 타입", allowableValues = {"campaign", "adgroup", "ad", "keyword"})
//    private AssetType assetType;

    private JobStatus status;
    private String keyword;
}