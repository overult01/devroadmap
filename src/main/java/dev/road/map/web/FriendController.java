package dev.road.map.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.Friend;
import dev.road.map.domain.FriendRepository;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.Type;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@RestController
public class FriendController {
	
	@Autowired
	ParseUser parseUser;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FriendRepository friendRepository;
	
	// 조회 
	// 친구 리스트 
	public ResponseEntity<List<User>> friendList(HttpServletRequest request){
		String email = parseUser.parseEmail(request);
		List<User> friendList = friendRepository.selectAllFriends(email);
		return ResponseEntity.ok().body(friendList);
	}

	// 다른 정원 둘러보기 리스트(랜덤 매칭)
//	public ResponseEntity<String> matchList(HttpServletRequest request){
//		
//		return ResponseEntity.ok().body("proposal success");
//
//	}

	// 설정 
	// 친구 신청
	public ResponseEntity<String> proposalTo(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);
		
		// 친구 신청할 유저 
		String email2 = request.getParameter("proposalTo");
		User user2 = userRepository.findByEmail(email2);
		
		Friend friend = Friend.builder()
			.user1(user)
			.user2(user2)
			.build();
		
		friendRepository.save(friend);
		return ResponseEntity.ok().body("proposal success");
	}

	// 받은 친구 신청 수락/거절 
	public ResponseEntity<String> acceptOrNot(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);
		
		// 받은 친구 신청 리스트
		List<Friend> proposalFrom = friendRepository.selectAllPropsalFrom(user);
		
		// 조회. 리스트를 어떻게 전달해주어야 할지 고민 
		for(Friend friend : proposalFrom) {
			User user1 = friend.getUser1();
			String nickname = user1.getNickname();
			String email_f1 = user1.getEmail();
			Field field = user1.getField();
			String profile = user1.getProfile();

			// 수락 or 거절
			String acceptOrNot = request.getParameter("acceptOrNot");
			if (acceptOrNot.equals("TRUE")) {
				friend.setAccept(true);
			}
			else {
				friend.setAccept(false);
			}
		}
		

		return ResponseEntity.ok().body("proposal success");
		
		// 

	}

	// 친구 끊기
	public ResponseEntity<String> disconnect(HttpServletRequest request){
		
		return ResponseEntity.ok().body("proposal success");

	}
	
	// 다른 정원 둘러보기 on/off
	public ResponseEntity<String> matchOrNot(HttpServletRequest request){
		
		return ResponseEntity.ok().body("proposal success");

	}

	// 정원사 검색
	public ResponseEntity<String> search(HttpServletRequest request){
		
		return ResponseEntity.ok().body("proposal success");
	}
}
