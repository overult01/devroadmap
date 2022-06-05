package dev.road.map.config.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import dev.road.map.domain.member.Member;
import dev.road.map.domain.member.MemberRepository;
import dev.road.map.domain.member.Provider;
import dev.road.map.dto.MemberDTO;

// 모듈화
@Component
public class ParseMember {

	Member member;
	
	@Autowired
	MemberRepository memberRepository;
	
	// oauthId파싱
	public String parseOauthId(Authentication auth) {
		DefaultOAuth2User authorization = (DefaultOAuth2User) auth.getPrincipal();

		String oauthId = null;
		String customerId = null;
		
		// 구글
		if(auth.getName().length() <= 40 & memberRepository.findByOauthId("google_" + auth.getName().toString()) != null) { 
			oauthId = "google_" + auth.getName();
			return oauthId;
		}
		
		// 카카오 
		else {
			customerId = authorization.getName();
			oauthId = "kakao_" + customerId;
			
			// 네이버 (임시 숫자 크기 지정)
			Map<String, Object> naverMap = null;
			if (customerId.length() >= 40) { 
				naverMap = authorization.getAttributes();
				naverMap = (Map<String, Object>) naverMap.get("response");
				customerId = (String) naverMap.get("id");
				oauthId = "naver_" + customerId;
			}
		}

		return oauthId;
	}
	
	// Member 파싱 
	public MemberDTO parseMemberDTO(Authentication auth) {
		DefaultOAuth2User authorization = (DefaultOAuth2User) auth.getPrincipal();

		String oauthId = null;
		Provider provider = null; 
		String nickname = null; 
		String email = null; 
		String customerId = null;

		// 구글
		if(auth.getName().length() <= 40 & memberRepository.findByOauthId("google_" + auth.getName().toString()) != null) { 
			oauthId = "google_" + auth.getName();
			provider = Provider.google;
			
			return 
			MemberDTO.builder()
			.oauthId(oauthId)
			.provider(provider)
			.nickname(nickname)
			.email(email)
			.build();
		}
		
		// 카카오 
		else {
			customerId = authorization.getName();
			oauthId = "kakao_" + customerId;
			provider = Provider.kakao;
			
			// 네이버 (임시 숫자 크기 지정)
			Map<String, Object> naverMap = null;
			if (customerId.length() >= 40) { 
				naverMap = authorization.getAttributes();
				naverMap = (Map<String, Object>) naverMap.get("response");
				customerId = (String) naverMap.get("id");
				oauthId = "naver_" + customerId;
				provider = Provider.naver;
			}
			
			return 
			MemberDTO.builder()
			.oauthId(oauthId)
			.provider(provider)
			.nickname(nickname)
			.email(email)
			.build();
		}

	}
	
}
