package dev.road.map.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.UserDTO;
import dev.road.map.service.UserService;

@RestController
public class IndexController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenProvider tokenprovider;
	
	@Value("${jwt.secret}")
    private String secret; // 숨김처리	
	
    @RequestMapping("jwt/create")
    public String jwtcreate(Authentication auth) {
    	return null;
    }
    
    @RequestMapping("/signup")
    public ResponseEntity<?> authenticate(HttpServletRequest request, UserDTO userDTO) {
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
    	
    	System.out.println("email: " + email);
    	
    	// 유저 아이디가 있는지 확인 
    	if (userRepository.findByEmail(email) != null) {
			return ResponseEntity.badRequest().body("used email");
		}
    	// DB 저장
    	User user = User.builder()
	    	.email(email)
	    	.nickname(null)
	    	.build();
    	
    	userRepository.save(user);
    	
    	// 토큰 생성
    	String token = tokenprovider.generateToken(user);
    	return ResponseEntity.ok()
    			.header("jwtToken", token)
    			.body("com");
    }
    
    @RequestMapping("/signin")
    public ResponseEntity<?> signin(HttpServletRequest request) {
//    	String email = request.getParameter("email");
//    	String password = request.getParameter("password");    	
//    	// 아직 비밀번호 암호화 전 
//    	User user = userService.getByCredential(email, password);
    	
    	// 요청에서 토큰 가져오기 
    	String bearerToken = request.getHeader("Authorization");
    	String token = null;
    	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
		}
    	String email;
    	
    	if (token!=null) {
    		// 토큰이 위조된 경우 예외 발생 
    		email = tokenprovider.verifyTokenAndGetOauthid(token);

    		return ResponseEntity.ok()
    			.body("com");
		}
		return ResponseEntity.badRequest().body("error");
    	
    }
}
