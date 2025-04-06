package com.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequestDto {
    private String refreshToken;
}