package dev.road.map.domain.user;

import java.sql.Timestamp;

import lombok.Getter;

@Getter
public class SessionUser {

//    private Long id;
    private String oauthid;    
    private Provider provider;
    private String nickname;
    private String email;
    private Type type;
    private Field field;
    private String profile;
    private Boolean unmatching;
    private String pin;
    private Timestamp joindate;
    private Role role;
    
	public SessionUser(User member) {
//		this.id = member.getId();
		this.oauthid = member.getOauthid();
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