package dev.road.map.dto;

import java.sql.Timestamp;

import dev.road.map.domain.member.Field;
import dev.road.map.domain.member.Member;
import dev.road.map.domain.member.Provider;
import dev.road.map.domain.member.Role;
import dev.road.map.domain.member.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class MemberDTO {

	private Long id;
    private String oauthId;  
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
	public MemberDTO(String oauthId, Provider provider, String nickname, String email) {
        this.oauthId = oauthId;
        this.provider = provider; 
        this.nickname = nickname; 
        this.email = email; 
    }    
}
