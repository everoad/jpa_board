package com.study.server.core.config.security;

import com.study.server.core.helper.MyUtils;
import com.study.server.core.model.ApiResponse;
import com.study.server.core.model.ErrorMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        MyUtils.writeResponseJSON(response, new ApiResponse<>(new ErrorMessage(authException.getMessage())));
    }

}
