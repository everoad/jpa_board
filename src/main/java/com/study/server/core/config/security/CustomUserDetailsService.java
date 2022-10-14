package com.study.server.core.config.security;

import com.study.server.domain.entity.Member;
import com.study.server.modules.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
        }
        return new MemberAdapter(optionalMember.get());
    }

}
