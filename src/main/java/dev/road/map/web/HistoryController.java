package dev.road.map.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.road.map.commons.ParseUser;
import dev.road.map.config.security.TokenProvider;
import dev.road.map.domain.History;
import dev.road.map.domain.HistoryRepository;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;
import dev.road.map.service.UserService;

@RestController
public class HistoryController {

	@Autowired
	public ObjectMapper mapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenProvider tokenprovider;

	@Autowired
	HistoryRepository historyRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	ParseUser parseUser;

	// 정원 기록 리스트 
    @RequestMapping("/history")
    public ResponseEntity<?> completeHistory (HttpServletRequest request) throws JsonProcessingException{
    	
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	List<History> histories = historyRepository.findByUser(user);
    	for(History history : histories) {
    		history.getSubject();
    		history.getCompletedate();
    	}
    	
    	// 자바 객체 -> json 변환 
        String result = mapper.writeValueAsString(histories);
    	
    	// 현재 로그인한 유저의 완료한 과목 리스트 
		return ResponseEntity.ok().body(result);
    }
}
