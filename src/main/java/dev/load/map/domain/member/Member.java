package dev.load.map.domain.member;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String oauthid;    
    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String nickname;
    private String email;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Field field;
    private String profile;
    private Boolean unmatching;
    private String pin;
    private LocalDateTime joindate;
    @Enumerated(EnumType.STRING)
    private Role role;
    
    // 필수값만 있는 생성자 (가입시)
    @Builder
	public Member(String oauthid, Provider provider, String nickname, String email) {
        this.oauthid = oauthid;
        this.provider = provider; 
        this.nickname = nickname; 
        this.email = email; 
    }


	public String getRoleString() {
		return this.role.toString();
	}
}
