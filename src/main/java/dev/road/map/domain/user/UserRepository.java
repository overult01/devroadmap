package dev.road.map.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.dto.UserDTO;

public interface UserRepository extends JpaRepository<User, Long>{

    // 닉네임 조회
    public User findByNickname(String nickname);    
    
    public User findByEmailAndPassword(String email, String password);

	public User findByEmail(String email);
	
	public User save(User user);
}