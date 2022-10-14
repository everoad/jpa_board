package com.study.server.domain.dto.board;

import com.study.server.domain.dto.member.MemberDto;
import com.study.server.domain.entity.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class BoardDetailDto {
    private Integer id;
    private String title;
    private String content;
    private MemberDto createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    private List<BoardFileDto> fileList;

    @Builder
    public BoardDetailDto(Integer id, String title, String content, List<BoardFileDto> fileList,
                          MemberDto createdBy, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileList = fileList;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .fileList(fileList)
                .build();
    }

    public static BoardDetailDto toDto(Board board) {
        return BoardDetailDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdBy(board.getCreatedBy() != null ? MemberDto.toDto(board.getCreatedBy()) : null)
                .fileList(board.getFileList().stream().map(BoardFileDto::toDto).collect(Collectors.toList()))
                .createdTime(board.getCreatedTime())
                .modifiedTime(board.getModifiedTime())
                .build();
    }
}
