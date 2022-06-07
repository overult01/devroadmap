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

    private String password;
    private String email;
//    private String nickname;
//    private Type type;
//    private Field field;
    
    // 필수값만 있는 생성자 (가입시)
//    @Builder
//	public UserDTO(String userid, String nickname, String email) {
//        this.userid = userid;
//        this.nickname = nickname; 
//        this.email = email; 
//    }  
//   
    public User ToUser(UserDTO userDTO) {
    	return User.builder()
//    	.nickname(userDTO.getNickname())
    	.email(userDTO.getEmail())
    	.build();
    }
}
