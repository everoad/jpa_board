package com.study.server.domain.dto.board;

import com.study.server.domain.entity.board.Board;
import com.study.server.domain.entity.board.BoardFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BoardFileDto {

    private Integer fileId;
    private String originName;
    private String path;


    @Builder
    public BoardFileDto(Integer fileId, String originName, String path) {
        this.fileId = fileId;
        this.originName = originName;
        this.path = path;
    }

    public BoardFile toEntity(Board board) {
        return BoardFile.builder()
                .id(fileId)
                .board(board)
                .originName(originName)
                .path(path)
                .build();
    }

    public static BoardFileDto toDto(BoardFile boardFile) {
        return BoardFileDto.builder()
                .fileId(boardFile.getId())
                .originName(boardFile.getOriginName())
                .path(boardFile.getPath())
                .build();
    }
}
