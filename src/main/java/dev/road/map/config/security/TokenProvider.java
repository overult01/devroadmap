package dev.road.map.config.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import dev.road.map.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider{

	@Value("${jwt.secret}")
    private String secret; // 숨김처리 
	
    private static final String AUTHORITIES_KEY = "auth";
	
    // JWT 토큰 생성 
    public String generateToken(User user) {
        
    	//Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("Email", user.getEmail());
        payloads.put("Nickname", user.getNickname());
        
        // 기한은 지금부터 1일 
        Date expireDate = Date.from(
        		Instant.now()
        		.plus(1, ChronoUnit.DAYS));

        byte[] secretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
        
//        Authentication authentication;
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
        
        // 토큰 builder
		String jwt = Jwts.builder()
        		.setHeader(headers)
                .claim(AUTHORITIES_KEY, user.getRoleString()) // 권한
        		.setSubject(user.getEmail()) // 토큰용도 
        		.setExpiration(expireDate)
        		.signWith(signingKey, SignatureAlgorithm.HS256)
        		.compact(); // 토큰생성
        
        System.out.println("jwt: " + jwt);

        return jwt;
    }
    
//    // 토큰 내 권한 리턴
    public Authentication getAuthentication(String token) {
    	Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(token)
                .getBody();
 
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
 
        User principal = new User(claims.getSubject(), "", authorities);
 
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // JWT 토큰 검증
    public String verifyTokenAndGetOauthid(String token) {
        // Claims: payload에 담긴 정보 
    	// 토큰을 디코딩, 파싱한 후 토큰의 위조 여부를 확인
    	// 이후 subject 즉 userid를 리턴.
    	Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // userid
    }
    
    // JWT 토큰 권한 추출
    public String verifyTokenAndGetRole(String token) {
        // Claims: payload에 담긴 정보 
    	// 토큰을 디코딩, 파싱한 후 토큰의 위조 여부를 확인
    	// 이후 subject 즉 userid를 리턴.
    	Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(token)
                    .getBody();
            return (String) claims.get(AUTHORITIES_KEY); // 토큰 내 권한을 String으로 반환 
    }

}