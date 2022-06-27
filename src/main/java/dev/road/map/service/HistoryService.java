package dev.road.map.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.road.map.domain.history.History;
import dev.road.map.domain.history.HistoryRepository;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@Service
public class HistoryService {

	@Autowired
	HistoryRepository historyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public String subjectComplete(int subject, User user) {
    	History history = historyRepository.findByUserAndSubject(user, subject); 
    	
    	String result = null;
    	
    	// 유저가 해당 과목에 대해 최초로 완료 체크를 한 경우 
    	if ( history.getIsdelete() == null) {
    		History.builder()
    				.user(user)
    				.subject(subject)
    				.isdelete(false)
    				.build();
    		
    		result = "new subject";
		}
    	
    	// 기존에 체크 -> 해제 -> 체크 한 경우 (최초 체크가 아닌경우)
    	else {
			history.setIsdelete(false);
			result = "existing subject";
		}
    	
    	historyRepository.save(history);
		
    	// user 진도율(progessRate)에도 반영
		List<History> histories = historyRepository.findByUserAndIsdelete(user, false);
		int completeCnt = histories.size();
		int progessRate = completeCnt * 100 / 19;
		
		user.setProgressRate(progessRate);
		userRepository.save(user);
		
		return result;
	}
	
	public Boolean subjectCompleteWithdraw(User user, int subject) {
    	
		History history = historyRepository.findByUserAndSubject(user, subject);
    	history.setIsdelete(true);
    	historyRepository.save(history);

    	// user 진도율(progessRate)에도 반영
		List<History> histories = historyRepository.findByUserAndIsdelete(user, false);
		int completeCnt = histories.size();
		int progessRate = completeCnt * 100 / 19;
		user.setProgressRate(progessRate);
		userRepository.save(user);
		
		return true;
	}
	
	public JsonObject completeHistory(User user) {
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
    	jsonObject.addProperty("user_email", user.getEmail());
    	jsonObject.addProperty("user_field", user.getField().toString());
    	jsonObject.add("complete_subjects", jsonArray);
    	
//    	{"user_email":"hello@gmail.com","user_field":"back","complete_subjects":[{"object":1,"completedate":"2022-06-24 04:42:07.0"},{"object":3,"completedate":"2022-06-26 18:01:07.0"}]}    	
    	System.out.println(jsonObject);
    	
    	return jsonObject;
	}
	
	public JsonObject SubjectCompleteCheck(User user, int subject) {
		    	
		// 응답 생성 
		JsonObject jsonObject = new JsonObject();
				
		History result = historyRepository.findByUserAndSubject(user, subject);
		
		// 완료했으면
		// {"user_email":"hello@gmail.com","object":1,"resp":"ok"}
		if (result != null) {
			jsonObject.addProperty("user_email", user.getEmail());
			jsonObject.addProperty("object", subject);
			jsonObject.addProperty("resp", "ok");
		}
		// 완료하지 않았으면
		//{"user_email":"overult01@gmail.com","object":2,"resp":"not yet"}
		else {
			jsonObject.addProperty("user_email", user.getEmail());
			jsonObject.addProperty("object", subject);
			jsonObject.addProperty("resp", "not yet");			
		}
		
		System.out.println(jsonObject);
		
		return jsonObject;
	}

}
