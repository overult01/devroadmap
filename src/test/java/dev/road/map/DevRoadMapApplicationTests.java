package dev.road.map;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.road.map.domain.user.Provider;
import dev.road.map.domain.user.User;

@ExtendWith(SpringExtension.class) // 스프링부트, JUnit사이 연결
@SpringBootTest
class DevRoadMapApplicationTests {

//	@Autowired
//	UserRepository memberRepository;
//	
//	@After
//	public void cleanup() {
//		memberRepository.deleteAll();
//	}
//	
//	@Test
//	void test() {
//		// given
//        String oauthid = "test_1234";
//        Provider provider = Provider.google; 
//        String nickname = "user"; 
//        String email = "email@gmail.com"; 
//        
//        memberRepository.save(User.builder()
//        		.oauthid(oauthid)
//        		.provider(provider)
//        		.email(email)
//        		.nickname(nickname)
//        		.build());
//        
//        // when
//        List<User> memberList = memberRepository.findAll();
//        
//        // then
//        User member = memberList.get(0);
//        assertThat(member.getOauthid().equals(oauthid));
//        assertThat(member.getProvider().equals(provider));
//        
//	}
}
