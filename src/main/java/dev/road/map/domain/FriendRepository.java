package dev.road.map.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.road.map.domain.user.User;

public interface FriendRepository extends JpaRepository<Friend, Long>{

	// 본인신청 & 삭제 n & 수락 y + 본인 수락 & 삭제 n & 수락 y
	// 친구 리스트 조회
	@Query(value=
			// 본인 신청 
			"select u.nickname, u.field, u.profile "
			+ "from user AS u JOIN friend AS f "
			+ "ON u.email= f.email_f1 "
			+ "where f.email_f1=?1 " // 입력받은 email
			+ "AND f.accept=TRUE "
			+ "AND NOT f.isdelete=TRUE "
			+ "AND NOT u.isdelete=TRUE "
			+ "UNION ALL "
			// 본인 수락
			+ "select u.nickname, u.field, u.profile "
			+ "from user AS u JOIN friend AS f "
			+ "ON u.email= f.email_f2 "
			+ "where f.email_f1=?2 " // 입력받은 email
			+ "AND f.accept=TRUE "
			+ "AND NOT f.isdelete=TRUE "
			+ "AND NOT u.isdelete=TRUE"
			, nativeQuery = true) // 컬럼명 email_f1로 바꿀 예정 
	public List<User> selectAllFriends(String email);

	// 다른 정원 둘러보기 리스트(랜덤 매칭)
	
	
	// 친구 신청	
	public Friend save(Friend friend);
}
