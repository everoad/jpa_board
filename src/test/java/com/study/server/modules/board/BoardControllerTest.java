package com.study.server.modules.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.common.BaseControllerTest;
import com.study.server.domain.dto.board.BoardDetailDto;
import com.study.server.domain.dto.board.BoardFileDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends BaseControllerTest {

    private final String baseUrl = "/boards/";

    public BoardControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    @Order(1)
    @Test
    public void 게시글_등록() throws Exception {
        //given
        BoardFileDto fileDto = BoardFileDto.builder()
                .originName("hello.png")
                .path("/adsf/hello.png")
                .build();

        BoardDetailDto saveDto = BoardDetailDto.builder()
                .title("Hello World!")
                .content("Hello Content!")
                .fileList(Collections.singletonList(fileDto))
                .build();

        //when & then
        this.postRequest(baseUrl, saveDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").isNotEmpty())
                .andDo(document("board/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("authorization")
                        ),
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("fileList").description("첨부 파일 목록"),
                                fieldWithPath("fileList[].originName").description("파일명"),
                                fieldWithPath("fileList[].path").description("파일 경로")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("data.id").description("게시글 ID")
                        )
                ));
    }

    @Order(2)
    @Test
    public void 게시글_목록_조회() throws Exception {
        //given
        BoardFileDto fileDto1 = BoardFileDto.builder()
                .originName("hello1.png")
                .path("/adsf/hello1.png")
                .build();

        BoardDetailDto boardDto = BoardDetailDto.builder()
                .title("Hello World!!")
                .content("Hello Content!!")
                .fileList(Collections.singletonList(fileDto1))
                .build();

        // when
        this.postRequest(baseUrl, boardDto);

        // then
        this.getRequest(baseUrl)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("data[*].title", containsInRelativeOrder(boardDto.getTitle())))
                .andDo(document("board/list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("authorization")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("data[].id").description("게시글 ID"),
                                fieldWithPath("data[].title").description("게시글 제목"),
                                fieldWithPath("data[].createdTime").description("게시글 등록일시"),
                                fieldWithPath("data[].modifiedTime").description("게시글 수정일시"),
                                fieldWithPath("data[].createdBy.id").description("작성자 ID"),
                                fieldWithPath("data[].createdBy.name").description("작성자 이름")
                        )
                ))
        ;
    }

    @Order(3)
    @Test
    public void 게시글_수정() throws Exception {
        // given
        BoardFileDto fileDto = BoardFileDto.builder()
                .originName("안녕.png")
                .path("/path/assadfklsd.png")
                .build();

        BoardDetailDto boardDto = BoardDetailDto.builder()
                .title("Hello World Edit!")
                .content("Hello Content Edit!")
                .fileList(Collections.singletonList(fileDto))
                .build();

        // when
        this.patchRequest(baseUrl + 1, boardDto)
                .andExpect(status().isOk());

        // then
        this.getRequest(baseUrl + 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value(boardDto.getTitle()))
                .andExpect(jsonPath("data.content").value(boardDto.getContent()))
                .andExpect(jsonPath("data.fileList").isArray())
                .andExpect(jsonPath("data.fileList[0].originName").value(fileDto.getOriginName()))
                .andExpect(jsonPath("data.fileList[0].path").value(fileDto.getPath()));
    }

    @Order(4)
    @Test
    public void 게시글_삭제() throws Exception {
        // when
        this.deleteRequest(baseUrl + 1)
                .andExpect(status().isOk());

        // then
        this.getRequest(baseUrl + 1)
                  .andExpect(status().isNotFound());
    }

}
