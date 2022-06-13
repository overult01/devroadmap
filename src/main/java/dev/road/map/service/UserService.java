package dev.road.map.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.Type;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
    
	@Autowired
    PasswordEncoder passwordEncoder;
    
	// 서비스를 이용한 유저 저장(user, email이 제대로 입력되었는지, 기존에 가입된 email인지 체크)
	public User create(final User user) {
		if (user == null || user.getEmail() == null) { // 입력된 user가 없는 경우 
			throw new RuntimeException("Invalid arguments");
		}
		final String email = user.getEmail();
		if (userRepository.findByEmail(email) != null) { // 이미 해당 이메일로 가입이 되어 있는 경우 
			throw new RuntimeException("Email already exist");
		}
		return userRepository.save(user);
	}
	
	// 입력받은 email로 유저 찾기-> matches메서드로 입력받은 password와 암호화된 password가 같은지 확인 
	public User getByCredential(final String email, final String password, final PasswordEncoder passwordEncoder) {
		final User originalUser = userRepository.findByEmail(email);
		
		//  BCryptPasswordEncoderd의 matchs메서드를 이용해 패스워드가 같은지 확인
		if (originalUser != null &&
				passwordEncoder.matches(password, originalUser.getPassword())) {
			return originalUser;
			
		}
		return null;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public User edit(HttpServletRequest request, User user) {
		String password = request.getParameter("password").trim();
		String profile = request.getParameter("profile").trim();
		String typeStr = request.getParameter("type").trim();
		String fieldStr = request.getParameter("field").trim();
		
		// 수정 정보가 있을 때만 반영 
		if (!password.isEmpty()) {
			user.setPassword(password);
		}
		if (!profile.isEmpty()) {
			user.setProfile(profile);
		}		
		if (!typeStr.isEmpty()) {
			Type type;
			if (Type.hours2.equals(typeStr)) {
				type = Type.hours2;
			}
			else if (Type.hours4.equals(typeStr)) {
				type = Type.hours4;
			}
			else {
				type = Type.hours8;				
			}
			user.setType(type);
		}
		if (!fieldStr.isEmpty()) {
			Field field;
			if (Field.back.equals(fieldStr)) {
				field = Field.back;
			}
			else {
				field = Field.front;
			}
			user.setField(field);
		}
		// 변경된 유저 정보 저장
		userRepository.save(user);
		return user;
		
	}
}
