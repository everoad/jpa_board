package com.study.server.modules.memberConnection;

import com.study.server.core.token.AuthToken;
import com.study.server.core.token.AuthTokenProvider;
import com.study.server.domain.entity.Member;
import com.study.server.domain.entity.MemberConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberConnectionService {

    private final MemberConnectionRepository connectionRepository;
    private final AuthTokenProvider authTokenProvider;

    @Transactional
    public MemberConnection insertConnection(Member member) {
        AuthToken authToken = authTokenProvider.createAuthToken(member.getId());
        MemberConnection connection = new MemberConnection(member, authToken);
        connectionRepository.save(connection);
        return connection;
    }

    public Optional<MemberConnection> selectConnectionByMember(Member member) {
        return connectionRepository.findByMemberAndAccessTokenExpiredAtGreaterThan(member, LocalDateTime.now());
    }

    public Optional<MemberConnection> selectConnectionByAccessToken(String accessToken) {
        return connectionRepository.findByAccessToken(accessToken);
    }


    public Optional<MemberConnection> selectConnectionByAccessTokenAndRefreshToken(String accessToken, String refreshToken) {
        return connectionRepository.findByAccessTokenAndRefreshToken(accessToken, refreshToken);
    }

    @Transactional
    public void deleteConnectionByAccessToken(String accessToken) {
        connectionRepository.deleteByAccessToken(accessToken);
    }

    @Transactional
    public void deleteConnection(MemberConnection connection) {
        connectionRepository.deleteById(connection.getId());
    }
}
