package com.study.server.modules.member;

import com.study.server.core.annotation.CurrentUser;
import com.study.server.core.helper.MyMediaType;
import com.study.server.core.model.ApiResponse;
import com.study.server.domain.dto.member.MemberDto;
import com.study.server.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/members", produces = MyMediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/me")
    public ApiResponse<MemberDto> me(@CurrentUser Member member) {
        MemberDto memberDto = MemberDto.toDto(member);
        return new ApiResponse<>(memberDto);
    }
}
