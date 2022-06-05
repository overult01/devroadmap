package dev.road.map.config.security.oauth.provider;

import java.util.Map;

import dev.road.map.domain.member.Provider;

/**
 * @author Jungmin, Yang
 *
 */

public class KakaoUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;
	private Map<String, Object> attributesAccount;
	private Map<String, Object> attributesProfile;
	
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>)attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>)attributes.get("properties");
    }

    // Provider(구글, 네이버, 카카오 중 1)
	@Override
	public Provider getProvider() {
		return Provider.kakao;
	}

	@Override
	public String getNick() {
		return (String) attributesProfile.get("nickname");
	}

	@Override
	public String getEmail() {
		return (String) attributesAccount.get("email");
	}

	@Override
	public String getOauthId() {
		return String.valueOf(attributes.get("id"));
	}

}