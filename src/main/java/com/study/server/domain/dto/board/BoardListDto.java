package com.study.server.domain.dto.board;

import com.study.server.domain.dto.member.MemberDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardListDto {

    private Integer id;
    private String title;
    private MemberDto createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    @QueryProjection
    public BoardListDto(Integer id, String title, String createdById, String createdByNm, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.createdBy = MemberDto.builder().id(createdById).name(createdByNm).build();
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }
}
