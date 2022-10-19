package com.study.server.domain.entity;

import com.study.server.core.token.AuthToken;
import org.springframework.util.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id", nullable = false, updatable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String accessToken;

    @Column(columnDefinition = "TEXT", nullable = false)
    public String refreshToken;

    @Column(nullable = false)
    private LocalDateTime accessTokenExpiredAt;

    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiredAt;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @Builder
    public MemberConnection(Member member, AuthToken authToken) {
        Assert.notNull(member, "member is null.");
        Assert.notNull(authToken, "authToken is null.");
        this.member = member;
        this.accessToken = authToken.getAccessToken();
        this.refreshToken = authToken.getRefreshToken();
        this.accessTokenExpiredAt = authToken.getAccessTokenExpiredAt();
        this.refreshTokenExpiredAt = authToken.getRefreshTokenExpiredAt();
    }
}
