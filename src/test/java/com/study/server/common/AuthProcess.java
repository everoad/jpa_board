package com.study.server.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.core.model.ApiResponse;
import com.study.server.core.token.AuthTokenResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthProcess {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public AuthProcess(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken() throws Exception {
        // Given
        Map<String, Object> content = Map.of("id", "tester", "password", "1234");
        // When & Then
        String result = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.accessToken").exists())
                .andExpect(jsonPath("data.refreshToken").exists())
                .andExpect(jsonPath("data.accessTokenExpiredAt").exists())
                .andExpect(jsonPath("data.refreshTokenExpiredAt").exists())
                .andReturn().getResponse().getContentAsString();
        ApiResponse<AuthTokenResponse> authToken = objectMapper.readValue(result, new TypeReference<>() {});
        return authToken.getData().getAccessToken();
    }


}
