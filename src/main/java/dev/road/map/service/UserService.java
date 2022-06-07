package dev.road.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public void save(final User user) {};
	
	public User getByCredential(final String email, final String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}
}
