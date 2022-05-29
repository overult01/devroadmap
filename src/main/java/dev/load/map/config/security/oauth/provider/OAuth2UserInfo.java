package dev.load.map.config.security.oauth.provider;

import dev.load.map.domain.member.Provider;

/**
 * @author Jungmin, Yang
 *
 */

// OAuth2.0 제공자(구글, 네이버, 카카오)들 마다 응답해주는 속성값이 달라서 공통 타입으로 제작.
public interface OAuth2UserInfo {	
	
	String getOauthId();
	Provider getProvider();
	String getNick();
	String getEmail();
}