package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ModifiedDto extends RegisteredDto {

    @Schema(description = "수정일자")
    protected String modifiedDate;

    @Schema(description = "수정자")
    protected UserDto.UserSummaryDto modifier;
}
