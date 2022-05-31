package dev.road.map.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.domain.member.Member;
import dev.road.map.domain.member.MemberRepository;
import dev.road.map.domain.member.Provider;

@RestController
public class IndexController {

	@Autowired
	MemberRepository memberRepository;
	
	@GetMapping("/save")
	public void save() {
		Member member = Member.builder()
				.email(null)
				.nickname("test")
				.oauthId("test_1234")
				.provider(Provider.google)
				.build();
		
		LocalDateTime curDateTime = LocalDateTime.now();
		member.setJoindate(curDateTime);
		
		memberRepository.save(member);
	}
	
}
