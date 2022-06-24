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
	UserService userService;

	@Autowired
	ParseUser parseUser;
	
	@Value("${frontDomain}")
	String frontDomain;

	// 유저가 완료한 과목들(숫자)의 전체 리스트 조회
    @RequestMapping("/history")
    public ResponseEntity<?> completeHistory (HttpServletRequest request) throws JsonProcessingException{
    	
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	// 유저의 필드(백/ 프론트)
    	String filed = user.getField().toString();
    	
    	// 응답 생성 
    	JsonObject jsonObject = new JsonObject();
    	JsonArray jsonArray = new JsonArray();    	
    	
    	List<History> histories = historyRepository.findByUser(user);
    	for(History history : histories) {
    		JsonObject jsonObject_inner = new JsonObject();
    		jsonObject_inner.addProperty("object", history.getSubject());
    		jsonObject_inner.addProperty("resp", "ok");
    		jsonArray.add(jsonObject_inner);
    		
    		jsonObject.addProperty("user_email", email);
    		jsonObject.addProperty("user_field", filed);
    		jsonObject.add("complete_subjects", jsonArray);
    	}
    	
    	// {"user_email":"hello@gmail.com", "user_field":"back", "complete_subjects":[{"object":1,"resp":"ok"},{"object":3,"resp":"ok"}]}
    	System.out.println(jsonObject);
        
    	// 현재 로그인한 유저의 완료한 과목 리스트 
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
    }
    
    // 로그인한 유저의 과목별 완료여부 조회
    @RequestMapping("/history/subject/compelete/check")
    public ResponseEntity<?> SubjectCompleteCheck (HttpServletRequest request, String object){
    	
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	    	
    	// 응답 생성 
    	JsonObject jsonObject = new JsonObject();
    	
    	// 파라미터로 전달받은 String 값을 Long으로 형변환
    	Long subject = (long) Integer.parseInt(object);
    	
    	History result = historyRepository.findByUserAndSubject(user, subject);
    	
    	// 완료했으면
    	// {"user_email":"hello@gmail.com","object":1,"resp":"ok"}
    	if (result != null) {
    		jsonObject.addProperty("user_email", email);
    		jsonObject.addProperty("object", subject);
    		jsonObject.addProperty("resp", "ok");
		}
    	// 완료하지 않았으면
    	//{"user_email":"overult01@gmail.com","object":2,"resp":"not yet"}
    	else {
    		jsonObject.addProperty("user_email", email);
    		jsonObject.addProperty("object", subject);
    		jsonObject.addProperty("resp", "not yet");			
		}
    	
    	System.out.println(jsonObject);
        
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body(jsonObject.toString());
    }
}
