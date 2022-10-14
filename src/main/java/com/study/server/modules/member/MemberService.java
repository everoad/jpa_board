package com.study.server.modules.member;

import com.study.server.domain.dto.member.MemberDto;
import com.study.server.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDto getMember(String id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다.");
        }
        Member member = optionalMember.get();
        return MemberDto.toDto(member);
    }

    @Transactional
    public void addMember(MemberDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if (optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 아이디입니다.");
        }
        Member member = memberDto.toEntity();
        member.updatePassword(member.getPassword(), passwordEncoder);
        memberRepository.save(member);
    }
}
