package dev.road.map.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.buf.Utf8Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.Friend;
import dev.road.map.domain.FriendRepository;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.FriendService;

@RestController
public class FriendController {
	
	@Autowired
	ParseUser parseUser;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FriendRepository friendRepository;
	
	@Autowired
	FriendService friendService;
	
	@Value("${frontDomain}")
	String frontDomain;
	
	// 조회 
	// 친구 리스트 
    @RequestMapping("/friend")
	public ResponseEntity<?> friendList(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);

		JsonObject jsonObject = friendService.friendList(user);
    	
    	// utf-8설정(닉네임에 한글 들어갈 수도 있으니)
		return ResponseEntity.ok()
				.header("Content-Type", "application/xml")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());	
	}

	// 다른 정원 둘러보기 리스트(랜덤 매칭) - 진도율(로드맵 기준) 10단위로 하고. 10단위가 없으면 다른 범위 탐색.(while). 백1, 프1(매일 달라지는)
//	public ResponseEntity<String> matchList(HttpServletRequest request){
//		
//		return ResponseEntity.ok().body("proposal success");
//
//	}

	// 설정 
	// 친구 신청
	public ResponseEntity<String> proposalTo(HttpServletRequest request){
		// 현재 로그인한 유저 
//		String email = parseUser.parseEmail(request);
//		User user_send_friend = userRepository.findByEmail(email);
//		
//		// 친구 신청할 유저 
//		String email2 = request.getParameter("proposalTo");
//		User user_recieve_friend = userRepository.findByEmail(email2);
//		
//		Friend friend = Friend.builder()
//				.user_send_friend(user_send_friend)
//				.user_recieve_friend(user_recieve_friend)
//				.build();
//		
//		friendRepository.save(friend);
		return ResponseEntity.ok().body("proposal success");
	}

	// 받은 친구 신청 수락/거절 
	public ResponseEntity<String> acceptOrNot(HttpServletRequest request){
//		// 현재 로그인한 유저 
//		String email = parseUser.parseEmail(request);
//		User user = userRepository.findByEmail(email);
//		
//		// 받은 친구 신청 리스트
//		List<Friend> proposalFrom = friendRepository.selectAllPropsalFrom(user);
//		
//		// 조회. 리스트를 어떻게 전달해주어야 할지 고민 
//		for(Friend friend : proposalFrom) {
//			User user1 = friend.getUser1();
//			String nickname = user1.getNickname();
//			String email_f1 = user1.getEmail();
//			Field field = user1.getField();
//			String profile = user1.getProfile();
//
//			// 수락 or 거절
//			String acceptOrNot = request.getParameter("acceptOrNot");
//			if (acceptOrNot.equals("TRUE")) {
//				friend.setAccept(true);
//			}
//			else {
//				friend.setAccept(false);
//			}
//		}
//		
		return ResponseEntity.ok().body("acceptOrNot success");
	}

	// 친구 끊기
	public ResponseEntity<String> disconnect(HttpServletRequest request){
//		// 현재 로그인한 유저 
//		String email = parseUser.parseEmail(request);
//		User user = userRepository.findByEmail(email);
//
//		// 끊을 친구 이메일
//		String friend_email = request.getParameter("friend");
//		User friend_user = userRepository.findByEmail(friend_email);
//		
//		Friend friend = Friend.builder()
//			.user1(friend_user)
//			.user2(friend_user)
//			.build();
//		friend.setIsdelete(true);
//		
//		// 변경사항 저장
//		friendRepository.save(friend);
		
		return ResponseEntity.ok().body("disconnect success");
	}
	
	// 다른 정원 둘러보기 on/off
	public ResponseEntity<String> matchOrNot(HttpServletRequest request){
		// 현재 로그인한 유저 
//		String email = parseUser.parseEmail(request);
//		User user = userRepository.findByEmail(email);
//		
//		if (user.getUnmatching().booleanValue()==true) {
//			user.setUnmatching(false);			
//		}
//		else if (user.getUnmatching().booleanValue()==false) {
//			user.setUnmatching(true);			
//		}
//		
//		// 변경사항 저장		
//		userRepository.save(user);
		return ResponseEntity.ok().body("matchOrNot success");

	}

	// 정원사 검색(닉네임 기반, unmatching 인 유저도 검색 불가)
	public ResponseEntity<?> search(HttpServletRequest request){
//		String search_nick = request.getParameter("searchNickname");
//		User user = friendRepository.search(search_nick);
		return ResponseEntity.ok().body("ok");
	}
}
