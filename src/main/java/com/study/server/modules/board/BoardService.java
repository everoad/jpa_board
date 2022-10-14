package com.study.server.modules.board;

import com.study.server.core.model.BaseSearch;
import com.study.server.domain.dto.board.BoardDetailDto;
import com.study.server.domain.dto.board.BoardListDto;
import com.study.server.domain.entity.board.Board;
import com.study.server.modules.board.repository.BoardQueryRepository;
import com.study.server.modules.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    public List<BoardListDto> getBoardList(BaseSearch search, Pageable pageable) {
        return boardQueryRepository.findAll(search, pageable);
    }

    public BoardDetailDto getBoard(Integer boardId) {
        Board board = findBoard(boardId);
        return BoardDetailDto.toDto(board);
    }

    @Transactional
    public BoardDetailDto addBoard(BoardDetailDto boardDetailDto) {
        Board board = boardDetailDto.toEntity();
        boardRepository.save(board);
        return BoardDetailDto.builder()
                .id(board.getId())
                .build();
    }

    @Transactional
    public void editBoard(Integer boardId, BoardDetailDto boardDetailDto) {
        Board board = findBoard(boardId);
        board.update(boardDetailDto);
    }

    @Transactional
    public void removeBoard(Integer boardId) {
        Board board = findBoard(boardId);
        boardRepository.delete(board);
    }

    private Board findBoard(Integer boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다.");
        }
        return optionalBoard.get();
    }
}
