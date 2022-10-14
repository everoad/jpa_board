package com.study.server.common;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@Import(RestDocsConfig.class)
@SpringBootTest
public class BaseControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public BaseControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private String authorization;

    @BeforeEach
    public void setUp() throws Exception {
        if (!StringUtils.hasText(this.authorization)) {
            AuthProcess authProcess = new AuthProcess(mockMvc, objectMapper);
            this.authorization = "Bearer " + authProcess.getAccessToken();
        }
    }

    public ResultActions getRequest(String url) throws Exception {
        return getRequest(url, null);
    }

    public <T> ResultActions getRequest(String url, T params) throws Exception {
        MockHttpServletRequestBuilder builder = get(url)
                .header(HttpHeaders.AUTHORIZATION, authorization);
        if (params != null) {
            MultiValueMap<String, String> ps = new LinkedMultiValueMap<>();
            ps.setAll(objectMapper.convertValue(params, new TypeReference<>() {}));
            builder.params(ps);
        }
        return mockMvc.perform(builder);
    }

    public <T> ResultActions postRequest(String url, T params) throws Exception {
        MockHttpServletRequestBuilder builder = post(url)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON);
        if (params != null) {
            builder.content(objectMapper.writeValueAsString(params));
        }
        return mockMvc.perform(builder);
    }

    public <T> ResultActions patchRequest(String url, T params) throws Exception {
        MockHttpServletRequestBuilder builder = patch(url)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON);
        if (params != null) {
            builder.content(objectMapper.writeValueAsString(params));
        }
        return mockMvc.perform(builder);
    }

    public ResultActions deleteRequest(String url) throws Exception {
        MockHttpServletRequestBuilder builder = delete(url)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON);
        return mockMvc.perform(builder);
    }

    public ResultActions getRequestAnonymous(String url, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = get(url);
        if (params != null) {
            MultiValueMap<String, String> ps = new LinkedMultiValueMap<>();
            ps.setAll(params);
            builder.params(ps);
        }
        return mockMvc.perform(builder);
    }

    public <T> ResultActions postRequestAnonymous(String url, T params) throws Exception {
        MockHttpServletRequestBuilder builder = post(url)
                .contentType(MediaType.APPLICATION_JSON);
        if (params != null) {
            builder.content(objectMapper.writeValueAsString(params));
        }
        return mockMvc.perform(builder);
    }

    public <T> T getResult(ResultActions actions, TypeReference<T> typeReference) throws Exception {
        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, typeReference);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
