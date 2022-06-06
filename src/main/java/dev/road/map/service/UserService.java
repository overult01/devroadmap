package dev.road.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository memberRepository;
	
	public void save(final User member) {}
}
