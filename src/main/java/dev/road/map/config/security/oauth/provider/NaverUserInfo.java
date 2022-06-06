package dev.road.map.config.security.oauth.provider;

import java.util.Map;

import dev.road.map.domain.user.Provider;

/**
 * @author Jungmin, Yang
 *
 */

public class NaverUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;
	
	// attributes 에 {id=.., email=.., name=..}
    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    // Provider(구글, 네이버, 카카오 중 1)
	@Override
	public Provider getProvider() {
		return Provider.naver;
	}

	@Override
	public String getNick() {
		return (String) attributes.get("name");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getOauthId() {
		return (String) attributes.get("id");
	}

}