package dev.road.map.domain.member;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import dev.road.map.dto.MemberDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "oauthid")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @Column(name = "oauthid", nullable = false)
    private String oauthid;  
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;
    
    @Column(nullable = false)
    private String nickname;
    
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Field field;

	@CreationTimestamp
	private Timestamp joindate;

    private String profile;
    private Boolean unmatching;
    private String pin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
