package com.study.server.domain.dto.member;

import com.study.server.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDto {
    private String id;
    private String name;
    private String password;

    @Builder
    public MemberDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .name(name)
                .password(password)
                .build();
    }

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }
}
