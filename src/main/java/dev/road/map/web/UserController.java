package dev.road.map.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	ParseUser parseUser;
	
	@Value("${directory}")
	private String directory;

	@Value("${memberImagePath}")
	String memberImagePath;
	
	@Value("${frontDomain}")
	String frontDomain;
	
//	현재 로그인한 사용자 정보 불러오기(비동기) - 클라이언트 측에서 jwt를 전달해주면, 복호화하여 사용자 정보(이메일, 닉네임, 프로필 사진, 필드-프론트/백) 전달
	@RequestMapping("/user/details")
	public ResponseEntity<?> userDetails(HttpServletRequest request) {
		
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	String nickname = user.getNickname();
    	String profile = user.getProfile();
    	Field field = user.getField();
    	
    	JsonObject jsonObject = new JsonObject();
    	
    	jsonObject.addProperty("email", email);
    	jsonObject.addProperty("nickname", nickname);
    	jsonObject.addProperty("profile", profile);
    	jsonObject.addProperty("field", field.toString());
    	
    	// {"email":"hello@gmail.com","nickname":"jj","profile":null,"field":"back"}
		return ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
	}
		
	// 닉네임 중복확인(비동기) - 회원 정보 수정시
    @RequestMapping("/edit/nickname/check")
    public ResponseEntity<?> nicknamecheck(HttpServletRequest request, String nickname){
    	// 현재 로그인한 유저
		if ("preflight request".equals(parseUser.parseEmail(request))) {
			return ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me")
					.header("Access-Control-Max-Age","3600")
					.body("preflight request");
		}
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	String existNick = user.getNickname();
    	System.out.println(existNick);

		JsonObject jsonObject = new JsonObject();
		String result = "duplicated";
		jsonObject.addProperty("email", email);

		// 사용가능한 닉네임일 때만 ok 반환
    	if (existNick == nickname || userRepository.findByNickname(nickname) == null) {
			result = "ok";
		}
		return ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
    }
    
    // 회원 정보 수정
    @RequestMapping("/edit/userdetails")
    public ResponseEntity<String> edit(HttpServletRequest request, MultipartFile profile){
    	// 닉네임(중복확인 먼저 해야 수정 가능. 프론트단에서 확인)
    	if (userService.edit(request, profile) != null) {
			ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
					.header("Access-Control-Allow-Credentials", "true")
					.body("edit success");
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
