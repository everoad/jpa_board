package com.study.server.core.token;

import com.study.server.domain.entity.MemberConnection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter @Setter
@NoArgsConstructor
public class AuthTokenResponse {

    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiredAt;
    private long refreshTokenExpiredAt;

    public AuthTokenResponse(MemberConnection connection) {
        this.accessToken = connection.getAccessToken();
        this.refreshToken = connection.getRefreshToken();
        this.accessTokenExpiredAt = toMilliseconds(connection.getAccessTokenExpiredAt());
        this.refreshTokenExpiredAt = toMilliseconds(connection.getRefreshTokenExpiredAt());
    }

    private long toMilliseconds(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
