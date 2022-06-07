package dev.road.map.domain.user;

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

import dev.road.map.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter @Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long id;
    
    @Id
    @Column(length = 150, name = "email", nullable = false)
    private String email; // 이메일을 아이디로 사용 
    
    private String password;
    
//    @Column(nullable = false)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @Enumerated(EnumType.STRING)
    private Field field;

	@CreationTimestamp
	private Timestamp joindate;

    private String profile;
    private Boolean unmatching;
    private String pin;
    
    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private Role role;
    
    // 필수값만 있는 생성자 (가입시)
    @Builder
	public User(String nickname, String email) {
        this.nickname = nickname; 
        this.email = email; 
    }

	public String getRoleString() {
		return this.role.toString();
	}
}
