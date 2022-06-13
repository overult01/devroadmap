package dev.road.map.config.security.jwt;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.road.map.config.security.PrincipalDetails;
import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.UserDTO;
import io.jsonwebtoken.io.IOException;

// 시큐리티가 가지고 있는 필터 중에 BasicAuthenticationFilter가 있음.
// 권한, 인증이 필요한 특정 주소를 요청하면 위 필터를 무조건 타게 되어있다.
// 만약에 권한, 인증이 필요한 주소가 아니면 이 필터를 안탄다.
public class JwtAuthorizationFilter extends OncePerRequestFilter{
	private UserRepository userRepository;
	private TokenProvider tokenProvider;
	
	// 생성자 
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenProvider tokenProvider) {
		super();
		this.userRepository = userRepository;
		this.tokenProvider = tokenProvider;
	}
	
	// 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 된다.
	// 해당 토큰에 대한 Validation을 검증하고 인증 정보를 Security Context에 저장
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException, java.io.IOException {

		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader : " + jwtHeader);
		
		// header가 있는지 확인 
		if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			System.out.println("==");
			chain.doFilter(request, response);
			return;
		}
		
		System.out.println("인증이나 권한이 필요한 주소가 요청 됨");
		

		// jwt 토큰을 검증해서 정상적인 사용자인지 확인
    	// 요청에서 토큰 가져오기 
    	String bearerToken = request.getHeader("Authorization");
    	String token = null;
    	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
			System.out.println("token: " + token);
			// 권한 확인, SecurityContext에 권한 정보 저장 
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		// 서명 
		String email = tokenProvider.verifyTokenAndGetUserEmail(token);
		
		// 서명이 정상적이면
		if (email != null) {
			System.out.println("email 정상");
			User user = userRepository.findByEmail(email);
			UserDTO userDTO = user.ToUserDTO(user);
			PrincipalDetails principalDetails = new PrincipalDetails(userDTO);
			// jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 강제로 만들기(username이 null이 아니기 때문) 
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// 시큐리티를 저장할 수 있는 세션에 접근하여 Authentication 객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}
		chain.doFilter(request, response);
	}
}	

// public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
//	private UserRepository userRepository;
//	private TokenProvider tokenProvider;
//	
//	// 생성자 
//	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenProvider tokenProvider) {
//		super(authenticationManager);
//		this.userRepository = userRepository;
//		this.tokenProvider = tokenProvider;
//	}
//	
//	// 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 된다.
//	// 해당 토큰에 대한 Validation을 검증하고 인증 정보를 Security Context에 저장
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws IOException, ServletException, java.io.IOException {
//
//		String jwtHeader = request.getHeader("Authorization");
//		System.out.println("jwtHeader : " + jwtHeader);
//		
//		// header가 있는지 확인 
//		if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
//			System.out.println("==");
//			chain.doFilter(request, response);
//			return;
//		}
//		
//		System.out.println("인증이나 권한이 필요한 주소가 요청 됨");
//		
//
//		// jwt 토큰을 검증해서 정상적인 사용자인지 확인
//    	// 요청에서 토큰 가져오기 
//    	String bearerToken = request.getHeader("Authorization");
//    	String token = null;
//    	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//			token = bearerToken.substring(7);
//			System.out.println("token: " + token);
//			// 권한 확인, SecurityContext에 권한 정보 저장 
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//		}
//		
//		// 서명 
//		String email = tokenProvider.verifyTokenAndGetOauthid(token);
//		
//		// 서명이 정상적이면
//		if (email != null) {
//			System.out.println("email 정상");
//			User user = userRepository.findByEmail(email);
//			UserDTO userDTO = user.ToUserDTO(user);
//			PrincipalDetails principalDetails = new PrincipalDetails(userDTO);
//			// jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 강제로 만들기(username이 null이 아니기 때문) 
//			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
//			
//			// 시큐리티를 저장할 수 있는 세션에 접근하여 Authentication 객체를 저장.
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//			
//		}
//		chain.doFilter(request, response);
//	}
//}