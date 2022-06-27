package dev.road.map.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.road.map.domain.Friend;
import dev.road.map.domain.FriendRepository;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@Service
public class FriendService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FriendRepository friendRepository;
	
	public JsonObject friendList(User user) {
		// 응답 생성 
    	JsonObject jsonObject = new JsonObject();
    	JsonArray jsonArray = new JsonArray();    	
    	
    	List<Friend> friends_send = friendRepository.findByUser1AndAcceptAndIsdelete(user, true, false);
    	List<Friend> friends_recieve = friendRepository.findByUser2AndAcceptAndIsdelete(user, true, false);

    	for(Friend friend_send : friends_send) {
    		JsonObject jsonObject_inner = new JsonObject();

    		// (본인이 친구 요청, 본인이 user1 = 본인이 user_send_friend)(상대방: user2)
    		User user_friend = friend_send.getUser2();
    		String friend_email = user_friend.getEmail();
    		String friend_nickname = user_friend.getNickname();
    		Field friend_field = user_friend.getField();
    		
    		User friend = userRepository.findByEmail(friend_email);
    		
    		jsonObject_inner.addProperty("friend_nickname", friend_nickname);
    		jsonObject_inner.addProperty("friend_email", friend_email);
    		jsonObject_inner.addProperty("friend_field", friend_field.toString());
    		jsonObject_inner.addProperty("friend_progressRate", friend.getProgressRate());  

    		System.out.println(jsonObject_inner);
    		jsonArray.add(jsonObject_inner);
    	}
    	
    	for(Friend friend_recieve : friends_recieve) {
    		JsonObject jsonObject_inner = new JsonObject();

    		// (상대방이 친구 요청, 본인이 user2 = 본인이 user_recieve_friend)(상대방: user1)
    		User user_friend = friend_recieve.getUser1();
    		String friend_email = user_friend.getEmail();
    		String friend_nickname = user_friend.getNickname();
    		Field friend_field = user_friend.getField();
    		
    		User friend = userRepository.findByEmail(friend_email);
    		
    		jsonObject_inner.addProperty("friend_nickname", friend_nickname);
    		jsonObject_inner.addProperty("friend_email", friend_email);
    		jsonObject_inner.addProperty("friend_field", friend_field.toString());  
    		jsonObject_inner.addProperty("friend_progressRate", friend.getProgressRate());  
    		
    		jsonArray.add(jsonObject_inner);
    	}

    	jsonObject.addProperty("user_email", user.getEmail());
    	jsonObject.add("friend_list", jsonArray);
    	
    	return jsonObject;
	}
}
