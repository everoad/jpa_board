package com.study.server.core.config.security;

import com.study.server.core.helper.MyUtils;
import com.study.server.core.model.ApiResponse;
import com.study.server.core.model.ErrorMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
        MyUtils.writeResponseJSON(response, new ApiResponse<>(errorMessage));
    }

}
