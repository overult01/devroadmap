package dev.road.map.web;

import java.util.List;
import java.util.Random;

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
    @RequestMapping("/friend/proposal/send")
	public ResponseEntity<String> proposalTo(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user_send_friend = userRepository.findByEmail(email);
		
		// 친구 신청할 유저 
		String email2 = request.getParameter("proposalTo");
		User user_receive_friend = userRepository.findByEmail(email2);
		
		Friend friend = Friend.builder()
				.user1(user_send_friend)
				.user2(user_receive_friend)
				.build();
		
		friendRepository.save(friend);

		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("ok");
	}

	// 받은 친구 신청 리스트 
    @RequestMapping("/friend/proposal/receive")
	public ResponseEntity<String> proposalFrom(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);
		
		JsonObject jsonObject = friendService.proposalFrom(user);
		
		// 조회. 받은 친구 신청
		return ResponseEntity.ok()
				.header("Content-Type", "application/xml")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());	
	}

	// 받은 친구 신청 수락/거절 
    @RequestMapping("/friend/proposal/acceptornot")
	public ResponseEntity<String> acceptOrNot(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);
		
		String friendNickname = request.getParameter("friendnickname");
		String acceptOrNot = request.getParameter("acceptornot"); // true / false
		Boolean acceptBoolean = Boolean.getBoolean(acceptOrNot);
		
		User friend_user = userRepository.findByNickname(friendNickname);
		
		Friend friend = friendRepository.findByUser1AndUser2(friend_user, user);
		friend.setAccept(acceptBoolean);
		
		friendRepository.save(friend);
		
		return ResponseEntity.ok()
				.header("Content-Type", "application/xml")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("ok");	
	}
    
	// 친구 끊기
    @RequestMapping("/friend/disconnect")
	public ResponseEntity<String> disconnect(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);

		// 끊을 친구 닉네임 
		String friendNickname = request.getParameter("friendnickname");
		User friend_user = userRepository.findByNickname(friendNickname);
		
		Friend friend = friendRepository.findByUser1AndUser2(friend_user, user);
		friend.setIsdelete(true);
		
		// 변경사항 저장
		friendRepository.save(friend);

		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("ok");
	}
	
	// 다른 정원 둘러보기 on/off
    @RequestMapping("/friend/matchornot")
	public ResponseEntity<String> matchOrNot(HttpServletRequest request){
		// 현재 로그인한 유저 
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);
		
		if (user.getUnmatching().booleanValue()==true) {
			user.setUnmatching(false);			
		}
		else if (user.getUnmatching().booleanValue()==false) {
			user.setUnmatching(true);			
		}
		
		// 변경사항 저장		
		userRepository.save(user);
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("ok");
	}

	// 정원사 검색(닉네임 기반 검색
	// (unmatching, isdelete가 true 인 유저는 검색 불가)
	@RequestMapping("/friend/search")
	public ResponseEntity<?> search(HttpServletRequest request){
		String searchNick = request.getParameter("searchnickname");

		User searchUser = userRepository.findByNicknameAndUnmatchingAndIsdelete(searchNick, false, false);

		System.out.println(searchUser);

		JsonObject jsonObject = new JsonObject();
		// 검색한 닉네임
		jsonObject.addProperty("search_user_nickname", searchNick);

		// 닉네임으로 검색한 유저가 있으면
		if (searchUser != null) {
			jsonObject.addProperty("result", "ok");
			jsonObject.addProperty("search_user_email", searchUser.getEmail());
			jsonObject.addProperty("search_user_field", searchUser.getField().toString());
			jsonObject.addProperty("search_user_progressrate", searchUser.getProgressRate());
			jsonObject.addProperty("search_user_joindate", searchUser.getJoindate().toString());
		}
		else {
			jsonObject.addProperty("result", "not exist nickname");
		}

		return ResponseEntity.ok()
				.header("Content-Type", "application/xml")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
	}

	// (랜덤매칭)다른 정원 둘러보기를 통해, 진도율이 유사한 다른 사용자를 프론트엔드, 백엔드1명씩 정해진 주기에 따라 추천
	// (unmatching, isdelete가 true 인 유저는 검색 불가)
	@RequestMapping("/friend/match")
	public ResponseEntity<?> matching(HttpServletRequest request) {

		// 현재 로그인한 유저
		String email = parseUser.parseEmail(request);
		User user = userRepository.findByEmail(email);

		Random random = new Random();

		float progressRate = user.getProgressRate()/10;
		// Math.round(double/float 숫자): 소수점 첫째자리에서 반올림하여 정수만든다.
		int calProgressRate = Math.round(progressRate) * 10; // 첫번째 정수자리에서 반올림하여 0 ~ 100까지 10의 배수로 만든다.

		// 매칭된 유저
		User matchUserBack = null;

		// back 매칭(진도율 기반)
		List<User> progressMatchBack;

		while (calProgressRate<=100) { // 100까지만 찾기
			progressMatchBack = userRepository.findByProgressRateBetweenAndFieldUnmatchingAndIsdelete(calProgressRate, calProgressRate+10, Field.back,  false, false);
			if (progressMatchBack != null){ // 매칭결과가 있으면 1명만 선택
				int cntBack = progressMatchBack.size();
				matchUserBack = progressMatchBack.get(random.nextInt(cntBack)); // 0~size-1에서 랜덤 정수 선택. 이 정수에 해당하는 인덱스를 가진 유저 선택.
				break;
			}
			// 매칭결과 없으면 calProgressRate+10 해서 범위를 달리해서 재검색
			calProgressRate += 10;
		}

		// 매칭된 유저
		User matchUserFront = null;

		// front 매칭(진도율 기반)
		List<User> progressMatchFront;

		while (calProgressRate<=100) { // 100까지만 찾기
			progressMatchFront = userRepository.findByProgressRateBetweenAndFieldUnmatchingAndIsdelete(calProgressRate, calProgressRate+10, Field.front,  false, false);
			if (progressMatchFront != null){ // 매칭결과가 있으면 1명만 선택
				int cntFront = progressMatchFront.size();
				matchUserFront = progressMatchFront.get(random.nextInt(cntFront)); // 0~size-1에서 랜덤 정수 선택. 이 정수에 해당하는 인덱스를 가진 유저 선택.
				break;
			}
			// 매칭결과 없으면 calProgressRate+10 해서 범위를 달리해서 재검색
			calProgressRate += 10;
		}

		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		// 매칭된 유저가 있으면
		if (matchUserBack != null) {
			JsonObject inner_jsonObject = new JsonObject();

			inner_jsonObject.addProperty("result", "ok");
			inner_jsonObject.addProperty("match_user_back_email", matchUserBack.getEmail());
			inner_jsonObject.addProperty("match_user_back_email", matchUserBack.getEmail());
		} else {
//			inner_jsonObject.addProperty("result", "not exist nickname");
		}

		if (matchUserFront != null) {
			JsonObject inner_jsonObject = new JsonObject();

			inner_jsonObject.addProperty("result", "ok");
			inner_jsonObject.addProperty("match_user_front_email", matchUserFront.getEmail());
			inner_jsonObject.addProperty("match_user_front_email", matchUserFront.getEmail());		} else {
//			inner_jsonObject.addProperty("result", "not exist nickname");
		}

		return ResponseEntity.ok()
				.header("Content-Type", "application/xml")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
	}
}
