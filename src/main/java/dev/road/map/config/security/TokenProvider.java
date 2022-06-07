package dev.road.map.config.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.road.map.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider{

	@Value("${jwt.secret}")
    private String secret; // 숨김처리 
	
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
        
        // 토큰 builder
		String jwt = Jwts.builder()
        		.setHeader(headers)
        		.setClaims(payloads)
        		.setSubject(user.getEmail()) // 토큰용도 
        		.setExpiration(expireDate)
        		.signWith(signingKey, SignatureAlgorithm.HS256)
        		.compact(); // 토큰생성
        
        System.out.println("jwt: " + jwt);

        return jwt;
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

}