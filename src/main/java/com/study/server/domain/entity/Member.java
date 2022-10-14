package com.study.server.domain.entity;

import com.study.server.domain.dto.member.MemberDto;
import com.study.server.domain.entity.base.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "member_id", nullable = false, updatable = false, length = 20)
    private String id;

    @Column(name = "password", nullable = false, length = 150)
    private String password;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Builder
    public Member(String id, String password, String name) {
        Assert.hasText(id, "id is empty");
        Assert.hasText(password, "password is empty");
        Assert.hasText(name, "name is empty");
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public void update(MemberDto memberDto) {
        this.name = memberDto.getName();
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedTime() == null;
    }

}
