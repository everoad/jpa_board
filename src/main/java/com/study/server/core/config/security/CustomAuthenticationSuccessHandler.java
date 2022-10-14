package com.study.server.core.config.security;

import com.study.server.core.helper.MyUtils;
import com.study.server.core.model.ApiResponse;
import com.study.server.core.token.AuthTokenResponse;
import com.study.server.domain.entity.Member;
import com.study.server.domain.entity.MemberConnection;
import com.study.server.modules.memberConnection.MemberConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberConnectionService memberConnectionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 사용자.
        MemberAdapter memberAdapter = (MemberAdapter) authentication.getPrincipal();
        Member member = memberAdapter.getMember();

        MemberConnection connection;
        Optional<MemberConnection> optionalConnection = memberConnectionService.selectConnectionByMember(member);
        if (optionalConnection.isEmpty()) {
            connection = memberConnectionService.insertConnection(member);
        } else {
            connection = optionalConnection.get();
        }
        MyUtils.writeResponseJSON(response, new ApiResponse<>(new AuthTokenResponse(connection)));
    }

}
