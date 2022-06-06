package dev.road.map.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	// 회원가입 미완료, 자진 탈퇴 
	GUEST("ROLE_GUEST", "회원가입 미완 회원"),
	
	// 회원가입 완료 회원 
	USER("ROLE_USER","회원"),;

    private final String key;
    private final String title;
}
