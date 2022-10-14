package com.study.server.core.token;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Component
public class AuthTokenProvider {

    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private static final Integer ACCESS_TOKEN_VALID_DATE = 7;
    private static final Integer REFRESH_TOKEN_VALID_DATE = 30;

    public String generateToken(String memberId) {
        byte[] randomBytes = new byte[32];
        // 랜덤 생성도 규칙이 있어서 서버를 재시작할 때마다 동일한 토큰이 순서대로 생성된다.
        // 이를 막기 위해 seed로 이메일 + UUID를 추가한다.
        String seed = memberId + UUID.randomUUID();
        secureRandom.setSeed(seed.getBytes());
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public AuthToken createAuthToken(String memberId) {
        LocalDateTime now = LocalDateTime.now();
        String accessToken = generateToken(memberId);
        String refreshToken = generateToken(memberId);
        LocalDateTime accessTokenExpiredAt = now.plusDays(ACCESS_TOKEN_VALID_DATE);
        LocalDateTime refreshTokenExpiredAt = now.plusDays(REFRESH_TOKEN_VALID_DATE);
        return new AuthToken(accessToken, refreshToken, accessTokenExpiredAt, refreshTokenExpiredAt);
    }

    public String parseToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.contains("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    public boolean isExpired(LocalDateTime expiredAt) {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
