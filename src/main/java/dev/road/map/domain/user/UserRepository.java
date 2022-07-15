package dev.road.map.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    // 닉네임 조회
    public User findByNickname(String nickname);    
    
    public User findByEmailAndPassword(String email, String password);

	public User findByEmail(String email);
	
	public User save(User user);

    // 정원사 검색(닉네임 기반)
    // (unmatching, isdelete가 true 인 유저는 검색 불가)
    User findByNicknameAndUnmatchingAndIsdelete(String searchNick, Boolean unmatching, Boolean isDelete);

    // (랜덤매칭)다른 정원 둘러보기를 통해, 진도율이 유사한 다른 사용자를 프론트엔드, 백엔드1명씩 정해진 주기에 따라 추천
    // (unmatching, isdelete가 true 인 유저는 검색 불가, 본인제외)
    List<User> findByProgressRateBetweenAndFieldAndUnmatchingAndIsdeleteAndEmailNot(int calProgressRate, int calProgressRate2, Field field, Boolean unmatching, Boolean isDelete, String email);
}