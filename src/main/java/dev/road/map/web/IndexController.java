package dev.road.map.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.domain.user.Provider;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@RestController
public class IndexController {

	@Autowired
	UserRepository memberRepository;
	
    @GetMapping("/hello")
    public String test() {
        return "Hello, world!";
    }
	
	// http://localhost:8080/login/oauth2/code/google?state=CcTjQR04JGhL9Eyw5Bza_hWElbiYWK_iS9-EUGC9haM%3D&code=4%2F0AX4XfWhn7YnbPllfNzd4YDjKnXyj_iq3nvoZN2WrYWu-yTGATVMGzZEp2OrA6l7Y8mjxpw&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&authuser=0&prompt=consent
	// http://localhost:3000/oauth2/authorization/google
//	@RequestMapping("/oauth2/{provider}")
//	public void login(@PathVariable("provider") Provider provider, @ResponseBody ) {
//		
//		if (provider.equals("google")) {
//			
//		}
//		else if (provider.equals("naver")) {
//			
//		}		
//		else if (provider.equals("kakao")) {
//			
//		}
//		else {
//			
//		}
//	}
	// private final HttpSession session = null;
	
	// http://localhost:3000/login/oauth2/code/kakao?code=LpfzSDCbgJ6uBDqfDSxwCq5o1fhtUNQGAFE70H8j58dxvbRFjsv1XJvx8DF-2LkL3zPoBQo9cuoAAAGBM3kivA
	// login/oauth2/code/naver?code=DAnqnoRrRPDDPpxraH&state=y_7EEvJ0nd
	
	// 로그인
//	@PostMapping("login/oauth2/code/{provider}")
//	public void login(@ResponseBody )
//	
//	@PostMapping("/join")
//	public ResponseEntity<?> join() {
//		User member = User.builder()
//				.email(null)
//				.nickname("test")
//				.oauthId("test_1234")
//				.provider(Provider.google)
//				.build();
//		
//		memberRepository.save(member);
//		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
//	}
	
//	@GetMapping("/logout")
//	public ResponseEntity<?> logout() {
//		session.invalidate();
//		return new ResponseEntity<String>("ok", HttpStatus.OK);
//	}
	
}
