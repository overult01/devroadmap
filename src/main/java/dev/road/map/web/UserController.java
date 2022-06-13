package dev.road.map.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ParseUser parseUser;
	
	@Autowired
	UserService userService;
	
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
    	// 현재 로그인한 유저 
    	User user = userRepository.findByEmail(email);
    	
    	// 닉네임(중복확인 먼저 해야 수정 가능. 프론트단에서 확인)
    	if (userService.edit(request, user) != null) {
    		return ResponseEntity.ok().body("edit success");
		};
    	
		return ResponseEntity.badRequest().body("edit failed");
    }
    
}
