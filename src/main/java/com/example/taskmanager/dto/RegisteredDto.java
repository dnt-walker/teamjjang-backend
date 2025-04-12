package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class RegisteredDto {
    @Schema(description = "작성일자")
    protected String registeredDate;
    
    @Schema(description = "등록자")
    protected UserDto.UserSummaryDto register;
}
