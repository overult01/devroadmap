package dev.road.map.domain.user;

import java.sql.Timestamp;

import lombok.Getter;

@Getter
public class SessionUser {

	private String email;
    private String nickname;
    private Type type;
    private Field field;
    private String profile;
    private Boolean unmatching;
    private Timestamp joindate;
    private Role role;
    
	public SessionUser(User member) {
		this.nickname = member.getNickname();
		this.email = member.getEmail();
		this.type = member.getType();
		this.field = member.getField();
		this.profile = member.getProfile();
		this.unmatching = member.getUnmatching();
		this.joindate = member.getJoindate();
		this.role = member.getRole();
	}
}
