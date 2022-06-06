package dev.road.map.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.dto.UserDTO;

public interface UserRepository extends JpaRepository<User, Long>{

    // 조회 
    public User findByOauthid(String oauthid);
    
    // 닉네임 조회
    public User findByNickname(String nickname);    
    
}