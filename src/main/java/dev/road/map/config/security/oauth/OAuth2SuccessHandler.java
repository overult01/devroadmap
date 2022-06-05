package dev.road.map.config.security.oauth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.road.map.config.security.ParseMember;
import dev.road.map.config.security.Token;
import dev.road.map.domain.member.Role;
import dev.road.map.dto.MemberDTO;
import dev.road.map.service.TokenService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler  extends SimpleUrlAuthenticationSuccessHandler{
//Success Handler에 진입했다는 것은, 로그인이 완료되었다는 뜻
	
    private final TokenService tokenService;

    @Autowired
    ParseMember parseMember;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        MemberDTO memberDTO = parseMember.parseMemberDTO(authentication);

        // 최초 로그인이라면 회원가입 처리를 한다.
        String targetUrl;

        Token token = tokenService.generateToken(memberDTO.getOauthId(), Role.user);
        
        targetUrl = UriComponentsBuilder.fromUriString("/")
                .queryParam("token", token)
                .build().toUriString();
        try {
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
    }
}
