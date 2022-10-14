package com.study.server.core.token;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class AuthToken {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpiredAt;
    private LocalDateTime refreshTokenExpiredAt;

    public AuthToken() {
    }

    public AuthToken(String accessToken, String refreshToken, LocalDateTime accessTokenExpiredAt, LocalDateTime refreshTokenExpiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }
}
