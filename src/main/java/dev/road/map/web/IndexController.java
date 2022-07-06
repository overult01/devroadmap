package dev.road.map.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.MailService;
import dev.road.map.service.UserService;

// @CrossOrigin(origins = "https://localhost:3000", allowedHeaders = "*") // 추가
@RestController
public class IndexController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenProvider tokenprovider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;   
	
	@Value("${jwt.secret}")
    private String secret; // 숨김처리	

	@Value("${frontDomain}")
	String frontDomain;
    
	// 닉네임 중복확인(비동기) - 회원 가입시
    @RequestMapping("/signup/nickname/")
    public ResponseEntity<?> nicknamecheck(HttpServletRequest request, String nickname){
    	
    	User user = userRepository.findByNickname(nickname);
    	
    	// 사용가능한 닉네임일 때만 ok 반환
    	if (user == null) {
    		return ResponseEntity.ok()
    				.header("Access-Control-Allow-Origin", frontDomain)
					.header("Access-Control-Allow-Credentials", "true")
    				.body("ok");
		}
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("fail");
    }
    	
	// 이메일 인증
    @RequestMapping("/signup/mail")
    public ResponseEntity<String> signupMail(String email, HttpServletRequest request) {

    	if (userRepository.findByEmail(email) == null) {
    		try {
    			// 이메일 인증
    			String authKey = mailService.SignupAuthMail(email);

    			User user = new User();
    			user.setEmail(email);
    			user.setAuthKey(authKey);
    			
    			// 서비스를 이용해 리포지터리에 사용자 저장(user, email이 제대로 입력되었는지, 기존에 가입된 email인지 체크)
    			userService.create(user);
    			
    			if (authKey != null) { // 이메일 발송 성공
    				return ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
    						.header("Access-Control-Allow-Credentials", "true")
    						.body("send mail");
    			}
    			else { // 이메일 발송 실패하면 에러 발생
    				throw new RuntimeException(); 
    			}
    			
    		} catch (Exception e) {
    			String err = e.toString();
				return ResponseEntity.badRequest().body(err);
    		}
    	}
    	else {
			return ResponseEntity.badRequest().body("sending mail is fail");
		}
    }

    // 인증 확인(사용자가 클릭) 
    @RequestMapping("/signup/mail/confirm")
    public ResponseEntity<String> comfirmMail(String email, String authKey, HttpServletResponse response, HttpServletRequest request) throws IOException {
		System.out.println("인증중");
		// AuthKey가 일치하면 Role 을 Mail로 업그레이드 
		User user = userRepository.findByEmail(email);
		if (user.getAuthKey().equals(authKey)) {
			user.setRole(Role.MAIL);
		}
		
		// 변화된 사항 저장(update)
		userRepository.save(user);
		// 회원가입 페이지로 리디이렉트
		String redirect_uri= frontDomain + "/signup";
		response.sendRedirect(redirect_uri);
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("signup2");
    }
    
	// 가입
    @SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/signup")
    public ResponseEntity<String> authenticate(HttpServletResponse response, HttpServletRequest request) {
    	try {
	    	String email = request.getParameter("email");
	    	String password = request.getParameter("password");
	    	String nickname = request.getParameter("nickname");	    	
	    	String fieldStr = request.getParameter("field");
	    		    	
	    	System.out.println(email);
	    	System.out.println(password);
	    	System.out.println(nickname);
	    	System.out.println(fieldStr);
	    	
	    	User user = userRepository.findByEmail(email);
	    	System.out.println(user);
	    	
	    	// 이메일 인증	완료 회원(mail)
			if (user.getRole() == Role.MAIL) { // 인증 성공해야지 회원가입 가능
				System.out.println("회원가입 중");
				user.setProgressRate(0); // 시작할 때 진도율은 0%
				user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
				user.setRole(Role.USER);
				user.setNickname(nickname);
				user.setUnmatching(false);
				user.setIsdelete(false);
				if (fieldStr.equals("front")) {
					user.setField(Field.front);
				}
				else {
					user.setField(Field.back);
				}
				
				// 변경사항 저장
				userRepository.save(user);
				return ResponseEntity.ok()
						.header("Access-Control-Allow-Origin", frontDomain)
						.header("Access-Control-Allow-Credentials", "true")
						.body("signup3(complete)");
			}
			else { // 이메일 인증 안되어 있으면
				throw new RuntimeException(); 
			}

			} catch (Exception e) {
    			String err = e.toString();
				return ResponseEntity.badRequest().body(err);
			}
    }
    
    @RequestMapping("/signin")
    public ResponseEntity<String> signin(HttpServletRequest request) {
    	
    	System.out.println("로그인중");
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");

    	System.out.println("email: " + email);
    	System.out.println("password: " + password);
    	
    	// 입력받은 email로 유저 찾기-> matches메서드로 입력받은 password와 암호화된 password가 같은지 확인 
    	User user = userService.getByCredential(email, password, passwordEncoder);
    	
    	if (user != null) {
    		// 토큰 생성
    		String token = tokenprovider.generateToken(user);
    		return ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
					.header("Access-Control-Allow-Credentials", "true")
    				.body(token);
		}
    	else { // 해당 user가 없거나, matches 로 확인한 비번이 틀리면
	    	return ResponseEntity.badRequest()
	    			.header("Access-Control-Allow-Origin", frontDomain)
					.header("Access-Control-Allow-Credentials", "true")
	    			.body("login failed");
		}
    	
    }

    // 토큰 검증
	@RequestMapping("/token/verify")
	public ResponseEntity<String> user(Authentication authentication, HttpServletRequest request) {
		  
		// 요청에서 토큰 가져오기 
		String bearerToken = request.getHeader("Authorization");
		String token = null;
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
			System.out.println("token: " + token);
		}
		String email;
		  
		if (token!=null) { // 정상인 경우
			  
			try {
				  // 토큰이 위조된 경우 예외 발생 
				email = tokenprovider.verifyTokenAndGetUserEmail(token);
				  
				User user = userRepository.findByEmail(email);
				SecurityContextHolder.getContext().setAuthentication(authentication);
//			// 세션 수정(권한 관리)
//			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//			authorities.add(new SimpleGrantedAuthority(user.getRoleString()));
//			// 세션에 변경사항 저장
//			SecurityContext context = SecurityContextHolder.getContext();
//			// UsernamePasswordAuthenticationToken
//			context.setAuthentication(
//					new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities));
//			HttpSession session = request.getSession(true);
//			// 위에서 설정한 값을 Spring security에서 사용할 수 있도록 세션에 설정
//			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
				  
				  return ResponseEntity.ok().header("Access-Control-Allow-Origin", frontDomain)
  						.header("Access-Control-Allow-Credentials", "true")
						  .body("com");
				
			} catch (Exception e) {
    			String err = e.toString();
				return ResponseEntity.badRequest()
						.header("Access-Control-Allow-Origin", frontDomain)
						.header("Access-Control-Allow-Credentials", "true")
						.body(err);
		  }
	  }
		return ResponseEntity.badRequest()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("error");
	}
}	
