package dev.road.map.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.UserService;

@RestController
public class UserController {

	// json 반환 
	@Autowired
	public ObjectMapper mapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	ParseUser parseUser;
	
	// 닉네임 중복확인(비동기) - 회원 정보 수정시
    @RequestMapping("/edit/nickname/check")
    public ResponseEntity<?> nicknamecheck(HttpServletRequest request, String nickname){
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	String existNick = user.getNickname();
    	
    	// 사용가능한 닉네임일 때만 ok 반환
    	if (existNick == nickname || userRepository.findByNickname(nickname) == null) {
    		return ResponseEntity.ok().body("ok");
		}
		return ResponseEntity.ok().body("fail");
    }
    
    // 회원 정보 수정
    @RequestMapping("/edit/userdetatils")
    public ResponseEntity<String> edit(HttpServletRequest request){
    	// 닉네임(중복확인 먼저 해야 수정 가능. 프론트단에서 확인)
    	if (userService.edit(request) != null) {
    		return ResponseEntity.ok().body("edit success");
		};
    	
		return ResponseEntity.badRequest().body("edit failed");
    }
    
    // 회원 탈퇴
    @RequestMapping("/edit/withdraw")
    public ResponseEntity<String> withdraw(HttpServletRequest request){
    	if (userService.withdraw(request)) {
    		return ResponseEntity.ok().body("withdraw success");			
		}
    	
		return ResponseEntity.badRequest().body("withdraw failed");
    }
    
}
