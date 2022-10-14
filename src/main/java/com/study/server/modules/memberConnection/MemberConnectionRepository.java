package com.study.server.modules.memberConnection;

import com.study.server.domain.entity.Member;
import com.study.server.domain.entity.MemberConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberConnectionRepository extends JpaRepository<MemberConnection, Integer> {

    Optional<MemberConnection> findByMemberAndAccessTokenExpiredAtGreaterThan(Member member, LocalDateTime accessTokenExpiredAt);

    Optional<MemberConnection> findByAccessToken(String accessToken);

    Optional<MemberConnection> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);

    void deleteByAccessToken(String accessToken);

}
