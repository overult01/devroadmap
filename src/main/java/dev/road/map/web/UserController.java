package dev.road.map.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.commons.ParseUser;
import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ParseUser parseUser;
	
	// 닉네임 중복확인(비동기)
    @RequestMapping("/edit/nickname/check")
    public String nicknamecheck(HttpServletRequest request, String nickname){
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	String existNick = user.getNickname();
    	
    	// 사용가능한 닉네임일 때만 ok 반환
    	if (existNick == nickname || userRepository.findByNickname(nickname) == null) {
			return "ok";
		}
		return "fail";  
    }
    
    @RequestMapping("/edit/userdetatils")
    public ResponseEntity<String> edit(HttpServletRequest request, String nickname){
    	String email = parseUser.parseEmail(request);

		return null;
    	
    }
    
}
