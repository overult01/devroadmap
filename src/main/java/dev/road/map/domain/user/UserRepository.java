package dev.road.map.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    // 닉네임 조회
    public User findByNickname(String nickname);    
    
    public User findByEmailAndPassword(String email, String password);

	public User findByEmail(String email);
	
	public User save(User user);

    // 정원사 검색(닉네임 기반)
    User findByNicknameAndUnmatchingAndIsdelete(String searchNick, Boolean unmatching, Boolean isDelete);

}