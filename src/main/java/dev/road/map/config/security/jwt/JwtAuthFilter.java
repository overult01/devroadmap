package dev.road.map.config.security.jwt;

import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import dev.road.map.dto.MemberDTO;
import dev.road.map.service.TokenService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    
	private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest)request).getHeader("Auth");

        if (token != null && tokenService.verifyToken(token)) {
            String oauthid = tokenService.getUid(token);

            // DB연동을 안했으니 이메일 정보로 유저를 만들어주겠습니다
            MemberDTO memberDTO = MemberDTO.builder()
                    .oauthid(oauthid)
                    .provider(null) // 임시 null
            		.nickname(null)
            		.email(null)
            		.build();

            //  jwt의 인증이 성공하면 SecurityContext에 해당 정보를 저장
            Authentication auth = getAuthentication(memberDTO);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
			chain.doFilter(request, response);
		} catch (java.io.IOException | ServletException e) {
			e.printStackTrace();
		}
    }

    public Authentication getAuthentication(MemberDTO member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}