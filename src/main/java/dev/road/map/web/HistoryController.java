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

	// 완료 과목 체크 표시 
	@RequestMapping("/subject/complete/add")
    public ResponseEntity<?> subjectComplete (HttpServletRequest request){
    	
		// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	// 숫자로 된 과목 표시 
    	int subject = Integer.parseInt(request.getParameter("subject"));
    	
    	History history = historyRepository.findByUserAndSubject(user, subject); 
    	// 유저가 해당 과목에 대해 최초로 완료 체크를 한 경우 
    	if ( history.getIsdelete() == null) {
    		History.builder()
    				.user(user)
    				.subject(subject)
    				.isdelete(false)
    				.build();
		}
    	
    	// 기존에 체크 -> 해제 -> 체크 한 경우 (최초 체크가 아닌경우)
    	else {
			history.setIsdelete(false);
		}
    	
    	historyRepository.save(history);
		
    	// user 진도율(progessRate)에도 반영
		List<History> histories = historyRepository.findByUserAndIsdelete(user, false);
		int completeCnt = histories.size();
		int progessRate = completeCnt * 100 / 19;
		
		user.setProgressRate(progessRate);
		userRepository.save(user);
		
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("subject is added");
		}
//		return ResponseEntity.badRequest()
//				.header("Access-Control-Allow-Origin", frontDomain)
//				.header("Access-Control-Allow-Credentials", "true")
//				.body("adding subject is failed");
//	}	
	
	// 완료 과목 체크 표시 해제
	@RequestMapping("/subject/complete/withdraw")
    public ResponseEntity<?> subjectCompleteWithdraw (HttpServletRequest request){
    	
		// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	// 숫자로 된 과목 표시 
    	int subject = Integer.parseInt(request.getParameter("subject"));
    	
    	History history = historyRepository.findByUserAndSubject(user, subject);
    	history.setIsdelete(true);
    	historyRepository.save(history);

    	// user 진도율(progessRate)에도 반영
		List<History> histories = historyRepository.findByUserAndIsdelete(user, false);
		int completeCnt = histories.size();
		int progessRate = completeCnt * 100 / 19;
		user.setProgressRate(progessRate);
		userRepository.save(user);
		
		return ResponseEntity.ok()
				.header("Access-Control-Allow-Origin", frontDomain)
				.header("Access-Control-Allow-Credentials", "true")
				.body("subject is withdrawed");
		}
	
	// 유저가 완료한 과목들(숫자)의 전체 리스트 조회
    @RequestMapping("/history")
    public ResponseEntity<?> completeHistory (HttpServletRequest request){
    	
    	// 현재 로그인한 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	
    	// 유저의 필드(백/ 프론트)
    	String filed = user.getField().toString();
    	
    	// 응답 생성 
    	JsonObject jsonObject = new JsonObject();
    	JsonArray jsonArray = new JsonArray();    	
    	
    	List<History> histories = historyRepository.findByUserAndIsdelete(user, false);
    	for(History history : histories) {
    		JsonObject jsonObject_inner = new JsonObject();
    		jsonObject_inner.addProperty("object", history.getSubject());
    		jsonObject_inner.addProperty("completedate", history.getCompletedate().toString());
    		jsonArray.add(jsonObject_inner);
    	}
    	jsonObject.addProperty("user_email", email);
    	jsonObject.addProperty("user_field", filed);
    	jsonObject.add("complete_subjects", jsonArray);
    	
//    	{"user_email":"hello@gmail.com","user_field":"back","complete_subjects":[{"object":1,"completedate":"2022-06-24 04:42:07.0"},{"object":3,"completedate":"2022-06-26 18:01:07.0"}]}    	
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
    	
    	// 파라미터로 전달받은 String 값을 int로 형변환
    	int subject = Integer.parseInt(object);
    	
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
