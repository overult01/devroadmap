package dev.load.map;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.load.map.domain.member.Member;
import dev.load.map.domain.member.Provider;

@ExtendWith(SpringExtension.class) // 스프링부트, JUnit사이 연결
@SpringBootTest
class DevLoadMapApplicationTests {

//	@Autowired
//	MemberRepository memberRepository;
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
//        memberRepository.save(Member.builder()
//        		.oauthid(oauthid)
//        		.provider(provider)
//        		.email(email)
//        		.nickname(nickname)
//        		.build());
//        
//        // when
//        List<Member> memberList = memberRepository.findAll();
//        
//        // then
//        Member member = memberList.get(0);
//        assertThat(member.getOauthid().equals(oauthid));
//        assertThat(member.getProvider().equals(provider));
//        
//	}
}
