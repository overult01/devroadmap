package dev.road.map.config.security.oauth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import dev.road.map.config.security.ParseUser;
import dev.road.map.config.security.TokenService;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.UserDTO;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
//Success Handler에 진입했다는 것은, 로그인이 완료되었다는 뜻
	
    private final TokenService tokenService;
    private final UserRepository memberRepository;
    
    @Autowired
    UserDTO memberDTO;
    
    @Autowired
    ParseUser parse;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException, NullPointerException {
        UserDTO memberDTO = parse.parseMemberDTO(authentication);
        User member = memberDTO.ToMember(memberDTO);

        String token = null;
        String targetUrl;
        String target = "http://localhost:3000";
        
        // 최초 로그인이라면 회원가입 처리, 토큰생성
        if (memberRepository.findByOauthid(memberDTO.getOauthid()) == null) {
        	System.out.println("최초 로그인으로 회원가입 진행");
        	
        	memberRepository.save(member);
        	memberRepository.flush();
        	
        	token = tokenService.generateToken(member);
        	
        	targetUrl = UriComponentsBuilder.fromUriString(target)
        			.queryParam("token", token)
        			.build().toUriString();
        	try {
        		getRedirectStrategy().sendRedirect(request, response, targetUrl);
        	} catch (java.io.IOException e) {
        		e.printStackTrace();
        	}
		}
    }
}
