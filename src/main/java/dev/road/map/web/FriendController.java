package dev.road.map.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.Friend;
import dev.road.map.domain.FriendRepository;
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
	public ResponseEntity<String> proposal(HttpServletRequest request){
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
		//
		return ResponseEntity.ok().body("proposal success");

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
