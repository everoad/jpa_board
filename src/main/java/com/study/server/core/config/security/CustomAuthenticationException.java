package com.study.server.core.config.security;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException {


    public CustomAuthenticationException(String msg) {
        super(msg);
    }

    public CustomAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
