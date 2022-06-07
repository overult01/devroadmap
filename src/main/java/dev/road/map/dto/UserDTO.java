package dev.road.map.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.Provider;
import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.Type;
import dev.road.map.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Component
public class UserDTO {

	private String token;
    private String oauthid;  
    private Provider provider;
    private String nickname;
    private String email;
    private Type type;
    private Field field;
	private Timestamp joindate;
    private String profile;
    private Boolean unmatching;
    private String pin;
    private Role role;
    
    // 필수값만 있는 생성자 (가입시)
    @Builder
	public UserDTO(String token, String oauthid, Provider provider, String nickname, String email) {
        this.oauthid = oauthid;
        this.provider = provider; 
        this.nickname = nickname; 
        this.email = email; 
    }  
   
    public User ToMember(UserDTO memberDTO) {
    	return User.builder()
    	.oauthid(memberDTO.getOauthid())
    	.email(memberDTO.getEmail())
    	.nickname(memberDTO.getNickname())
    	.provider(memberDTO.getProvider())
    	.build();
    }
}
