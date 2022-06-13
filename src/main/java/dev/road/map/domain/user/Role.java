package dev.road.map.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	MAIL("ROLE_MAIL", "이메일 인증회원"),
	
	// 회원가입 완료 회원 
	USER("ROLE_USER","회원가입 완료 회원"),;

    private final String key;
    private final String title;
}
