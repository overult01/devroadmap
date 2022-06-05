package dev.road.map.service;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import dev.road.map.config.security.Token;
import dev.road.map.domain.member.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService{
    private String secretKey = "token-secret-key";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token generateToken(String oauthId, Role role) {
        long tokenPeriod = 1000L * 60L * 10L; // 1일 
        long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L;

        Claims claims = Jwts.claims().setSubject(oauthId);
        claims.put("role", role);

        Date now = new Date();
        return new Token(
                "accesstoken",
                "refreshToken");
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}