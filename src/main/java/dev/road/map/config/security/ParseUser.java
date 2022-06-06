package dev.road.map.config.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import dev.road.map.domain.user.Provider;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.UserDTO;

// 모듈화
@Component
public class ParseUser {

	User member;
	
	@Autowired
	UserRepository memberRepository;
	
	// oauthid파싱
	public String parseOauthid(Authentication auth) {
		DefaultOAuth2User authorization = (DefaultOAuth2User) auth.getPrincipal();

		String oauthid = null;
		String customerId = null;
		
		// 구글
		if(auth.getName().length() <= 40 & memberRepository.findByOauthid("google_" + auth.getName().toString()) != null) { 
			oauthid = "google_" + auth.getName();
			return oauthid;
		}
		
		// 카카오 
		else {
			customerId = authorization.getName();
			oauthid = "kakao_" + customerId;
			
			// 네이버 (임시 숫자 크기 지정)
			Map<String, Object> naverMap = null;
			if (customerId.length() >= 40) { 
				naverMap = authorization.getAttributes();
				naverMap = (Map<String, Object>) naverMap.get("response");
				customerId = (String) naverMap.get("id");
				oauthid = "naver_" + customerId;
			}
		}

		return oauthid;
	}
	
	// User 파싱 
	public UserDTO parseMemberDTO(Authentication auth) {
		DefaultOAuth2User authorization = (DefaultOAuth2User) auth.getPrincipal();

		String oauthid = null;
		Provider provider = null; 
		String nickname = null; 
		String email = null; 
		String customerId = null;

		// 구글
		if(auth.getName().length() <= 40 & memberRepository.findByOauthid("google_" + auth.getName().toString()) != null) { 
			oauthid = "google_" + auth.getName();
			provider = Provider.google;
			
			return 
			UserDTO.builder()
			.oauthid(oauthid)
			.provider(provider)
			.nickname(nickname)
			.email(email)
			.build();
		}
		
		// 카카오 
		else {
			customerId = authorization.getName();
			oauthid = "kakao_" + customerId;
			provider = Provider.kakao;
			
			// 네이버 (임시 숫자 크기 지정)
			Map<String, Object> naverMap = null;
			if (customerId.length() >= 40) { 
				naverMap = authorization.getAttributes();
				naverMap = (Map<String, Object>) naverMap.get("response");
				customerId = (String) naverMap.get("id");
				oauthid = "naver_" + customerId;
				provider = Provider.naver;
			}
			
			return 
			UserDTO.builder()
			.oauthid(oauthid)
			.provider(provider)
			.nickname(nickname)
			.email(email)
			.build();
		}

	}
	
}
