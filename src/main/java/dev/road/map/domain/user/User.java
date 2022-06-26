package dev.road.map.domain.user;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

import dev.road.map.dto.UserDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter @Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
    
    @Id
    @Column(length = 120, name = "email", nullable = false)
    private String email; // 이메일을 아이디로 사용 
    
    private String password;
    
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    private Field field;

	@CreationTimestamp
	private Timestamp joindate;

	// 프로필사진
	@Getter(AccessLevel.NONE)
	private String profile;
	
    private Boolean unmatching;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private Boolean isdelete;
    
    private String authKey;
    
    // 진도율("history 테이블에서 완료한 과목 수 / 19 "에서 소수점 1자리에서 반올림한 백분율 값)
    private int progressRate;
    
    // 필수값만 있는 생성자 (가입시)
    @Builder
	public User(String password, Field field, String nickname, String email, Role role) {
    	this.password = password;
    	this.field = field;
        this.nickname = nickname; 
        this.email = email; 
        this.role = role;
    }

	public String getProfile() {
		if (profile == null || profile.equals("")) {
			return null;
		} else {
			return profile;
		}
	}
    
	public String getRoleString() {
		return this.role.toString();
	}
	
    public UserDTO ToUserDTO(User user) {
    	return UserDTO.builder()
//    		.nickname(user.getEmail())
    		.role(Role.USER)
    		.password(user.getPassword())
	    	.email(user.getEmail())
	    	.build();
    }

	public User(String subject, String string, Collection<? extends GrantedAuthority> authorities) {
	}    
}
