package dev.road.map.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.road.map.domain.member.Member;
import dev.road.map.domain.member.MemberRepository;
import dev.road.map.domain.member.Provider;

@ExtendWith(SpringExtension.class) // 스프링부트, JUnit사이 연결
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	
	@After
	public void cleanup() {
		memberRepository.deleteAll();
	}

	@Test
	void save() {
		
		// given
        String oauthId = "test_1234";
        Provider provider = Provider.google; 
        String nickname = "nick"; 
        String email = "nick@gmail.com"; 
        
        memberRepository.save(Member.builder()
        		.oauthId(oauthId)
        		.provider(provider)
        		.nickname(nickname)
        		.email(email)
        		.build());
        
        // when
        Member findMember = memberRepository.findByOauthId(oauthId);
        
        // then
        assertThat(findMember.getOauthId().equals(oauthId));
        assertThat(findMember.getProvider().equals(provider));
        assertThat(findMember.getNickname().equals(nickname));
        assertThat(findMember.getEmail().equals(email));
	}

}
