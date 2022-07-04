package dev.road.map.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.road.map.commons.ParseUser;
import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.history.History;
import dev.road.map.domain.history.HistoryRepository;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.HistoryService;
import dev.road.map.service.UserService;

@RestController
public class HistoryController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenProvider tokenprovider;

	@Autowired
	HistoryRepository historyRepository;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	UserService userService;

	@Autowired
	ParseUser parseUser;
	
	@Value("${frontDomain}")
	String frontDomain;

	// 완료 과목 체크 표시 
	@RequestMapping("/subject/complete/add")
    public ResponseEntity<?> subjectComplete (HttpServletRequest request){
    	
		// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	// 숫자로 된 과목 표시 
    	int subject = Integer.parseInt(request.getParameter("subject"));
    	
    	String result = historyService.subjectComplete(subject, user);
    	
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(result);
		}
	
	// 완료 과목 체크 표시 해제
	@RequestMapping("/subject/complete/withdraw")
    public ResponseEntity<?> subjectCompleteWithdraw (HttpServletRequest request){
    	
		// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	// 숫자로 된 과목 표시 
    	int subject = Integer.parseInt(request.getParameter("subject"));
    	
    	historyService.subjectCompleteWithdraw(user, subject);
		
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("subject is withdrawed");
		}
	
	// 유저가 완료한 과목들(숫자)의 전체 리스트 조회
    @RequestMapping("/history")
    public ResponseEntity<?> completeHistory (HttpServletRequest request){
    	
    	System.out.println("history");
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	    	
    	JsonObject jsonObject = historyService.completeHistory(user);
        
    	// 현재 로그인한 유저의 완료한 과목 리스트 
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
    }
}
