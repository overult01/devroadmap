package dev.road.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.road.map.domain.member.Member;
import dev.road.map.domain.member.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	public void save(final Member member) {}
}
