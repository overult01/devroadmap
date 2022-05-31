package dev.road.map.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.domain.member.MemberRepository;

@RestController("member")
public class MemberController {

	@Autowired
	MemberRepository memberRepository;
	
}
