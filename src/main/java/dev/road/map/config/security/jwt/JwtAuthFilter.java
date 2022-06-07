package dev.road.map.config.security.jwt;

import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.road.map.config.security.TokenService;
import dev.road.map.dto.UserDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    
	private final TokenService tokenService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        String token = request.getHeader("Auth");
    	String token = parseBearerToken(request);
    	
    	System.out.println("필터동작");
    	// 토큰 검사. jwt이므로 인가 서버에 요청하지 않고도 검증 가능.
        if (token != null && !token.equalsIgnoreCase(token)) {
        	// oauthid가져오기. 위조된 경우 예외 처리된다.
        	try {
        		String oauthid = tokenService.verifyTokenAndGetOauthid(token);
        		System.out.println("필터동작, 토큰존재 ");
        		// DB연동을 안했으니 이메일 정보로 유저를 만들어주겠습니다
        		UserDTO memberDTO = UserDTO.builder()
        				.oauthid(oauthid)
        				.provider(null) // 임시 null
        				.nickname(null)
        				.email(null)
        				.build();

        		//  jwt의 인증이 성공하면 SecurityContext에 해당 정보를 저장
        		// Authentication auth = getAuthentication(memberDTO);
        		Authentication auth = getAuthentication(memberDTO);
        		SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (IllegalArgumentException e) {
                System.out.println("JWT Token가져오기 실패");
			} catch (ExpiredJwtException e) {
                System.out.println("만료된 JWT Token");
			}
        }

        try {
        	filterChain.doFilter(request, response);
		} catch (java.io.IOException | ServletException e) {
			e.printStackTrace();
		}
    }

    private String parseBearerToken(HttpServletRequest request) {
    	String bearerToken = request.getHeader("Authorization");
    	
    	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public Authentication getAuthentication(UserDTO member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}