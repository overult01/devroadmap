package dev.road.map.config.security.oauth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import dev.road.map.config.security.oauth.provider.KakaoUserInfo;
import dev.road.map.config.security.oauth.provider.NaverUserInfo;
import dev.road.map.config.security.oauth.provider.OAuth2UserInfo;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.Provider;
import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.Type;
import dev.road.map.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OAuthAttributes {

    private Map<String,Object> attributes;
    private String nameAttributeKey;

    private Long id;
    private String oauthid;    
    private Provider provider;
    private String nickname;
    private String email;
    private Type type;
    private Field field;
    private String profile;
    private Boolean unmatching;
    private String pin;
    private LocalDateTime joindate;
    private Role role;
    private Boolean isdelete;
	
    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, Long id, String oauthid, Provider provider,
    		String nickname, String email, Type type, Field field,
    	    String profile, Boolean unmatching, String pin, LocalDateTime joinDate, Boolean isdelete, Role role) {
    	super();
    	this.attributes = attributes;
    	this.nameAttributeKey = nameAttributeKey;
    	this.oauthid = oauthid;
    	this.provider = provider;
    	this.nickname = nickname;
    	this.email = email;
    	this.isdelete = isdelete;
    }
    
    public static OAuthAttributes of(String Provider,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
    	if (Provider.equals("google")) {
    		return ofGoogle(userNameAttributeName, attributes);
		}
    	else if (Provider.equals("naver")) {
    		return ofNaver(userNameAttributeName, attributes);
		}
    	else if (Provider.equals("kakao")) {
    		return ofKakao(userNameAttributeName, attributes);
		}
    	else {
    		System.out.println("지원하지 않는 OAuth provider입니다.");
    		return null;
		}
    }
    
    // 구글 로그인
    public static OAuthAttributes ofGoogle(String userNameAttributeName,
                                           Map<String, Object> attributes) {
    	System.out.println("구글 로그인 호출");
        return OAuthAttributes.builder()
            	.oauthid ("google_"+(String) attributes.get("sub"))
            	.provider (Provider.google) 
            	.nickname ((String) attributes.get("name"))
            	.email ((String) attributes.get("email")) 
            	.isdelete (false) 
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    
    // 네이버 로그인 
    public static OAuthAttributes ofNaver(String userNameAttributeName,
            Map<String, Object> attributes) {
    	System.out.println("네이버 로그인 호출");
		OAuth2UserInfo oAuth2UserInfo = new NaverUserInfo((Map<String, Object>)attributes.get("response"));
		System.out.println("response: " + oAuth2UserInfo.getEmail());

		return OAuthAttributes.builder()
	            .oauthid ("naver_"+(String) oAuth2UserInfo.getOauthId())
	            .provider (Provider.naver) 
	            .nickname ((String) oAuth2UserInfo.getNick()) 
	            .email ((String) oAuth2UserInfo.getEmail()) 
	            .isdelete (false) 
	            .attributes(attributes)
	            .nameAttributeKey(userNameAttributeName)
	            .build();
    }

    // 카카오 로그인 
    public static OAuthAttributes ofKakao(String userNameAttributeName,
            Map<String, Object> attributes) {
    	System.out.println("카카오 로그인 호출");
		OAuth2UserInfo oAuth2UserInfo = new KakaoUserInfo((Map<String, Object>)attributes);
		System.out.println("response: " + oAuth2UserInfo.getEmail());

		return OAuthAttributes.builder()
	            .oauthid ("kakao_"+(String) oAuth2UserInfo.getOauthId())
	            .provider (Provider.kakao) 
	            .nickname ((String) oAuth2UserInfo.getNick()) 
	            .email ((String) oAuth2UserInfo.getEmail()) 
	            .isdelete (false) 
	            .attributes(attributes)
	            .nameAttributeKey(userNameAttributeName)
	            .build();
	}

	public User toEntity() {
        return User.builder()
        .oauthid (oauthid)
        .provider (provider) 
        .nickname (nickname) 
        .email (email) 
        .build();		
	}

	public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("oauthid", oauthid);
        map.put("name", nickname);
        map.put("email", email);
        map.put("provider", provider);

        return map;
	}
}
