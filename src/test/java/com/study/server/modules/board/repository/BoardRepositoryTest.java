package com.study.server.modules.board.repository;

import com.study.server.domain.dto.board.BoardDetailDto;
import com.study.server.domain.dto.board.BoardFileDto;
import com.study.server.domain.entity.board.Board;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestConstructor;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @WithUserDetails("tester")
    @Test
    public void 게시글_등록() throws Exception {
        BoardFileDto fileDto = BoardFileDto.builder()
                .originName("안녕.png")
                .path("/path/assadfklsd.png")
                .build();

        BoardDetailDto saveDto = BoardDetailDto.builder()
                .title("Hello World Edit!")
                .content("Hello Content Edit!")
                .fileList(Arrays.asList(fileDto, fileDto))
                .build();


        Board board = saveDto.toEntity();
        boardRepository.save(board);


//        Optional<Board> optionalBoard = boardRepository.findByTitle(board.getTitle());

        Optional<Board> optionalBoard = boardRepository.findByFileName(fileDto.getOriginName());

        assertTrue(optionalBoard.isPresent());
        assertEquals(optionalBoard.get().getContent(), saveDto.getContent());
    }
}
