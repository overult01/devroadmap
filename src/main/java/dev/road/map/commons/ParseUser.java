package dev.road.map.commons;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.user.User;

// 모듈화
@Component
public class ParseUser {

	@Autowired
	TokenProvider tokenProvider;
	
	User user;
	
	// 현재 로그인한 유저의 이메일(아이디 대체)
	public String parseEmail(HttpServletRequest request) {
       	String bearerToken = request.getParameter("Authorization");

//		String bearerToken = request.getHeader("Authorization");
    	String token = null;
    	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
			System.out.println("token: " + token);
    	}

		// preflight 요청일경우 jwt 미인증
		if (request.getMethod().equals("OPTIONS")) {
			System.out.println("preflight request");
			return "preflight request";
		}

		// preflight 요청 아닐 경우 jwt 인증
		String email = tokenProvider.verifyTokenAndGetUserEmail(token);
		System.out.println(email);
		return email;
	}
			
}
