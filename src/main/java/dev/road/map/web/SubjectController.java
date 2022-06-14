package dev.road.map.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Utf8;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.Subject;
import dev.road.map.domain.SubjectRepository;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@RestController
public class SubjectController {

	@Autowired
	public ObjectMapper mapper;
	
	@Autowired
	SubjectRepository subjectRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ParseUser parseUser;

	// 완료해야할 과목들이 담긴 로드맵(현재 로그인한 유저의 field에 맞게 제공. 메인 하단에서 확인 가능)
    @RequestMapping(value = "/roadmap", produces = "application/json; charset=utf8")
    public ResponseEntity<?> roadmapPerField(HttpServletRequest request) throws JsonProcessingException{
    	// 현재 유저 
    	String email = parseUser.parseEmail(request);
    	User user = userRepository.findByEmail(email);
    	Field field = user.getField();
    	
    	List<Subject> subjects = subjectRepository.findByField(field);
    	
    	// 자바 객체 -> json 변환 
        String result = mapper.writeValueAsString(subjects);
        
    	return ResponseEntity.ok().body(result);
    }

}