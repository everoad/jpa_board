package com.study.server.modules.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends BaseControllerTest {

    private final String baseUrl = "/members/";

    public MemberControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    @Test
    public void 사용자_조회() throws Exception {
        this.getRequest(baseUrl + "me", null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value("tester"))
                .andExpect(jsonPath("data.name").value("tester"));

    }
}
