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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.dto.LoginDTO;
import dev.road.map.service.MailService;
import dev.road.map.service.UserService;

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
    
	// 이메일 인증
    @RequestMapping("/signup/mail")
    public ResponseEntity<String> signupMail(String email, HttpServletRequest request) {

    	if (userRepository.findByEmail(email) == null) {
    		try {
    			// 이메일 인증
    			String authKey = mailService.SignupAuthMail(email);
    			if (authKey != null) { // 이메일 발송 성공
    				return ResponseEntity.ok().body("send mail");
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
		// DB 저장
		User user = new User();
		user.setEmail(email);
		user.setRole(Role.MAIL);
		
		// 서비스를 이용해 리포지터리에 사용자 저장(user, email이 제대로 입력되었는지, 기존에 가입된 email인지 체크)
		userService.create(user);
		// 회원가입 페이지로 리디이렉트
		String redirect_uri="/signup";
		response.sendRedirect(redirect_uri);
		return ResponseEntity.ok().body("signup2");
    }
    
	// 가입
    @RequestMapping("/signup")
    public ResponseEntity<String> authenticate(HttpServletResponse response, HttpServletRequest request, LoginDTO loginDTO) {
    	try {
	    	String email = request.getParameter("email");
	    	String password = request.getParameter("password");
	    	User user = userRepository.findByEmail(email);
	    	// 이메일 인증	완료 회원(mail)
			if (user.getRole() == Role.MAIL) { // 인증 성공해야지 회원가입 가능
				System.out.println("회원가입 중");
				// DB 저장
				user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
				user.setRole(Role.USER);
				user.setField(null); // 임시 
				user.setType(null);
				user.setNickname(null);
				
				userRepository.save(user);
				response.sendRedirect("/signin");
				return ResponseEntity.ok().body("signup3(complete)");
			}
			else { // 이메일 인증 안되어 있으면
				throw new RuntimeException(); 
			}

			} catch (Exception e) {
    			String err = e.toString();
				return ResponseEntity.badRequest().body(err);
			}
    }
    
	@ResponseBody
    @RequestMapping("/signin")
    public ResponseEntity<String> signin(HttpServletRequest request, LoginDTO loginDTO) {
    	
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
    		return ResponseEntity.ok()
    				.header("jwtToken", token)
    				.body("com");
		}
    	else { // 해당 user가 없거나, matches 로 확인한 비번이 틀리면
	    	return ResponseEntity.badRequest().body("login failed");
		}
    	
    }

    // 토큰 검증
	@ResponseBody
	@RequestMapping("/token/verify")
	public ResponseEntity<String> user(Authentication authentication,HttpServletRequest request) {
		  
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
				email = tokenprovider.verifyTokenAndGetOauthid(token);
				  
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
				  
				  return ResponseEntity.ok()
						  .body("com");
				
			} catch (Exception e) {
    			String err = e.toString();
				return ResponseEntity.badRequest().body(err);
		  }
		  // return ResponseEntity.badRequest().body("error");
	  }
		return ResponseEntity.badRequest().body("error");
	}
}	
// 권한관리(보류)
//    @RequestMapping("/user")
//    public ResponseEntity<?> user(HttpServletRequest request) {
//    	// 요청에서 토큰 가져오기 
//    	String bearerToken = request.getHeader("Authorization");
//    	String token = null;
//    	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//			token = bearerToken.substring(7);
//			System.out.println("token: " + token);
//		}
//    	
//    	if (token!=null) { // 정상인 경우
//    		// 토큰이 위조된 경우 예외 발생 
//    		String role = tokenprovider.verifyTokenAndGetRole(token);
//    		System.out.println(role);
//    		if (role.equals("USER")) {
//    			return ResponseEntity.ok().body("user");
//			}
//    	}
//		return ResponseEntity.badRequest().body("error");
//    }

