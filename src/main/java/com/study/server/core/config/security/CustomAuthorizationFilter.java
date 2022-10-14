package com.study.server.core.config.security;

import com.study.server.core.token.AuthTokenProvider;
import com.study.server.domain.entity.MemberConnection;
import com.study.server.modules.memberConnection.MemberConnectionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Getter
@Setter
public class CustomAuthorizationFilter extends GenericFilterBean {

    private AuthenticationEntryPoint authenticationEntryPoint;
    private MemberConnectionService memberConnectionService;
    private AuthTokenProvider authTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            String token = authTokenProvider.parseToken(httpServletRequest);
            if (StringUtils.hasText(token)) {
                Optional<MemberConnection> optionalMemberConnection = memberConnectionService.selectConnectionByAccessToken(token);
                if (optionalMemberConnection.isEmpty()) {
                    throw new CustomAuthenticationException("Access token is invalid");
                }
                MemberConnection memberConnection = optionalMemberConnection.get();
                if (authTokenProvider.isExpired(memberConnection.getAccessTokenExpiredAt())) {
                    throw new CustomAuthenticationException("Access token is expired");
                }
                MemberAdapter memberAdapter = new MemberAdapter(memberConnection.getMember());
                Authentication authentication = new UsernamePasswordAuthenticationToken(memberAdapter, memberAdapter.getPassword(), memberAdapter.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (AuthenticationException exception) {
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
            this.authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, exception);
        }
    }
}
