package com.study.server.core.config.security;

import com.study.server.domain.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class MemberAdapter extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Member member;

    public MemberAdapter(Member member) {
        super(member.getId(), member.getPassword(), authorities("MEMBER"));
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    private static Collection<? extends GrantedAuthority> authorities(String role) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }

}
