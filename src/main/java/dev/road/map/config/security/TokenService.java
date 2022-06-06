package dev.road.map.config.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenService{

	@Value("${jwt.secret}")
    private String secret; // 숨김처리 
	
	// private static final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	
    // JWT 생성 
    public String generateToken(User user) {
        
    	//Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("Oauthid", user.getOauthid());
        payloads.put("Email", user.getEmail());
        payloads.put("NickName",user.getNickname());
        
        System.out.println("secret: " + secret);
        
        // 기한은 지금부터 1일 
        Date expireDate = Date.from(
        		Instant.now()
        		.plus(1, ChronoUnit.DAYS));

        // 토큰 builder
        String jwt = Jwts.builder()
        		.setHeader(headers)
        		.setClaims(payloads)
        		.setSubject(user.getOauthid()) // 토큰용도 
        		.setExpiration(expireDate)
        		.signWith(SignatureAlgorithm.HS256, secret)
        		.compact(); // 토큰생성
        
        System.out.println("jwt: " + jwt);

        return jwt;
    }

    // 토큰 검증
    public String verifyTokenAndGetOauthid(String token) {
        // 토큰을 디코딩, 파싱한 후 토큰의 위조 여부를 확인
    	// 이후 subject 즉 oauthid를 리턴.
    	Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
    }

}