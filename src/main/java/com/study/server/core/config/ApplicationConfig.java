package com.study.server.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.study.server.core.config.security.MemberAdapter;
import com.study.server.core.handler.LoggingInterceptor;
import com.study.server.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(value = { AppProperties.class })
@RequiredArgsConstructor
public class ApplicationConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .excludePathPatterns("/files/**", "/h2-console/**");
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(@Value("${spring.mvc.format.date}") String dateFormat,
                                                                @Value("${spring.mvc.format.date-time}") String dateTimeFormat) {
        return builder -> {
            builder.failOnUnknownProperties(false);
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            // Request Body
            builder.deserializers(new LocalDateDeserializer(dateFormatter));
            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
            // Response Body
            builder.serializers(new LocalDateSerializer(dateFormatter));
            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
        };
    }

    @Bean
    public AuditorAware<Member> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof MemberAdapter adapter) {
                    return Optional.ofNullable(adapter.getMember());
                } else {
                    return Optional.empty();
                }
            }
            return Optional.empty();
        };
    }
}
