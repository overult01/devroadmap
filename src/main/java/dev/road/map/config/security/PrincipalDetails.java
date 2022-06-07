package dev.road.map.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.UserDTO;
import lombok.Data;

// 시큐리티가 /login 주소요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 되면 시큐리티 session을 만들어준다.(시큐리티 자체적인 세션. key값은 Security ContextHolder)
// 시큐리티 session에 들어갈 수 있는 오브젝트(정해져있음): Authentication 타입의 객체
// Authentication안에 UserDTO정보가 있어야 한다.
// UserDTO 오브젝트 타입(정해져있음) : UserDTODetails 타입 객체

// Security Session에 들어가는 객체 Authentication. 이에 들어가는 UserDTODetails 객체

@Data
public class PrincipalDetails implements UserDetails{ // PrincipalDetails가 두 타입을 묶어줄 수 있도록.(시큐리티 세션에 넣기위해)

	private UserDTO userDTO; // 콤포지션
	private Map<String, Object> attributes;
	private User user;
	private UserRepository userRepository;
	
	// 생성자(일반 로그인)
	public PrincipalDetails(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	// 해당 UserDTO의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userDTO.getRoleString();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return userDTO.getPassword();
	}

	
	@Override
	public String getUsername() {
		return userDTO.getNickname();
	}

	// 계정 만료 여부 
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠김 여부 
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 오래 되어있는지 
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화 되어있는지 
	@Override
	public boolean isEnabled() {
		
		// 예) 우리 사이트에서 1년 동안 회원이 로그인 안할 시 휴먼 계정 처리
		// if 현재시간 - 로그인시간 => 1년 초과: return false

		return true;
	}

}
