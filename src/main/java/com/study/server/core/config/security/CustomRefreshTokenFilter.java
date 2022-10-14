package com.study.server.core.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.core.token.AuthToken;
import com.study.server.core.token.AuthTokenProvider;
import com.study.server.domain.entity.MemberConnection;
import com.study.server.modules.memberConnection.MemberConnectionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Getter
@Setter
public class CustomRefreshTokenFilter extends AbstractCustomAuthenticationFilter {

    private MemberConnectionService connectionService;
    private AuthTokenProvider authTokenProvider;
    private ObjectMapper objectMapper;

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthToken authToken = getAuthToken(request);
        Optional<MemberConnection> optionalMemberConnection = connectionService.selectConnectionByAccessTokenAndRefreshToken(authToken.getAccessToken(), authToken.getRefreshToken());
        if (optionalMemberConnection.isEmpty()) {
            throw new CustomAuthenticationException("Invalid refresh token.");
        }
        MemberConnection memberConnection = optionalMemberConnection.get();
        if (authTokenProvider.isExpired(memberConnection.getRefreshTokenExpiredAt())) {
            throw new CustomAuthenticationException("Refresh token is expired.");
        }
        connectionService.deleteConnection(memberConnection);
        MemberAdapter memberAdapter = new MemberAdapter(memberConnection.getMember());
        return new UsernamePasswordAuthenticationToken(memberAdapter, memberAdapter.getPassword(), memberAdapter.getAuthorities());
    }

    private AuthToken getAuthToken(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getReader(), AuthToken.class);
        } catch (Exception ignored) {
            return new AuthToken();
        }
    }

}
