package com.study.server.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.core.token.AuthTokenProvider;
import com.study.server.modules.memberConnection.MemberConnectionService;
import com.study.server.core.config.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomUserDetailsService userDetailsService;
    private final MemberConnectionService memberConnectionService;
    private final AuthTokenProvider authTokenProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/files/**", "/h2-console/**", "/docs/index.html");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                           CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(refreshTokenFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authorizationFilter(authenticationEntryPoint), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .maximumSessions(10)
                .and()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() throws Exception {
        return new CustomAuthenticationEntryPoint();
    }

    public CustomRefreshTokenFilter refreshTokenFilter(AuthenticationManager authenticationManager) throws Exception {
        CustomRefreshTokenFilter filter = new CustomRefreshTokenFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/token", HttpMethod.POST.name()));
        filter.setConnectionService(memberConnectionService);
        filter.setAuthTokenProvider(authTokenProvider);
        filter.setObjectMapper(objectMapper);
        return filter;
    }

    public CustomAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", HttpMethod.POST.name()));
        return filter;
    }

    public CustomAuthorizationFilter authorizationFilter(CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        CustomAuthorizationFilter filter = new CustomAuthorizationFilter();
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setMemberConnectionService(memberConnectionService);
        filter.setAuthTokenProvider(authTokenProvider);
        return filter;
    }
}
