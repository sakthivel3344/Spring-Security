package com.example.spring_jwt.dto;

import lombok.Data;

@Data
public class AccessAndRefreshToken {
    private String accessToken;

    private String refreshToken;

    public AccessAndRefreshToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
