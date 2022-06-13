package dev.road.map.dto;

import org.springframework.stereotype.Component;

import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Component
public class UserDTO {

    private String password;
    private String email;
    private String nickname;
    private Role role;
//    private Type type;
//    private Field field;
    

//   
    public User ToUser(UserDTO userDTO) {
    	return User.builder()
	    	.email(userDTO.getEmail())
	    	.build();
    }

	public String getRoleString() {
		return this.role.toString();
	}    
}
