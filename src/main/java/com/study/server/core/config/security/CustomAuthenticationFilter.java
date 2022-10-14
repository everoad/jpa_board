package com.study.server.core.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.domain.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends AbstractCustomAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        MemberDto memberDto = getMemberRequest(request);
        if (!StringUtils.hasText(memberDto.getId()) || !StringUtils.hasText(memberDto.getPassword())) {
            throw new CustomAuthenticationException("Full authentication is required to access this resource.");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDto.getId(), memberDto.getPassword(), null);
        return getAuthenticationManager().authenticate(authentication);
    }

    private MemberDto getMemberRequest(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getReader(), MemberDto.class);
        } catch (IOException ignored) {
            return MemberDto.builder().build();
        }
    }
}
