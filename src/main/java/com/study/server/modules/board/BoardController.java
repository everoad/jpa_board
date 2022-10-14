package com.study.server.modules.board;

import com.study.server.core.helper.MyMediaType;
import com.study.server.core.model.ApiResponse;
import com.study.server.core.model.BaseSearch;
import com.study.server.domain.dto.board.BoardDetailDto;
import com.study.server.domain.dto.board.BoardListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/boards", produces = MyMediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ApiResponse<List<BoardListDto>> getBoardList(BaseSearch search, Pageable pageable) {
        List<BoardListDto> boardList = boardService.getBoardList(search, pageable);
        return new ApiResponse<>(boardList);
    }

    @GetMapping("/{boardId}")
    public ApiResponse<BoardDetailDto> getBoard(@PathVariable Integer boardId) {
        BoardDetailDto board = boardService.getBoard(boardId);
        return new ApiResponse<>(board);
    }

    @PostMapping
    public ApiResponse<BoardDetailDto> addBoard(@RequestBody BoardDetailDto boardDetailDto) {
        BoardDetailDto board = boardService.addBoard(boardDetailDto);
        return new ApiResponse<>(board);
    }

    @PatchMapping("/{boardId}")
    public ApiResponse<Void> editBoard(@PathVariable Integer boardId, @RequestBody BoardDetailDto boardDetailDto) {
        boardService.editBoard(boardId, boardDetailDto);
        return new ApiResponse<>();
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Void> removeBoard(@PathVariable Integer boardId) {
        boardService.removeBoard(boardId);
        return new ApiResponse<>();
    }

}
