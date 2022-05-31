package dev.road.map.domain.member;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class SessionMember {

    private Long id;
    private String oauthId;    
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
    
	public SessionMember(Member member) {
		this.id = member.getId();
		this.oauthId = member.getOauthId();
		this.provider = member.getProvider();
		this.nickname = member.getNickname();
		this.email = member.getEmail();
		this.type = member.getType();
		this.field = member.getField();
		this.profile = member.getProfile();
		this.unmatching = member.getUnmatching();
		this.pin = member.getPin();
		this.joindate = member.getJoindate();
		this.role = member.getRole();
	}
}
