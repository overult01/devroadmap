package dev.road.map.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.UserDTO;

// 앞서 시큐리티 설정에서 loginProcessingUrl("/login") 설정해둠
// 따라서 /login 요청 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadUserByUsername 함수가 실행 (규칙) 
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	// 시큐리티 session(내부 Authentication (내부 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("username(=email): " + email);
		User user = userRepository.findByEmail(email);
		if(user != null) {
			System.out.println("로그인중");
			UserDTO userDTO = user.ToUserDTO(user);
			return new PrincipalDetails(userDTO);
		}
		System.out.println("로그인실패");
		return null;
	}

	
}