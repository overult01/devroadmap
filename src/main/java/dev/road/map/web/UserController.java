package dev.road.map.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.road.map.domain.user.UserRepository;

@RestController("member")
public class UserController {

	@Autowired
	UserRepository memberRepository;
	
}
