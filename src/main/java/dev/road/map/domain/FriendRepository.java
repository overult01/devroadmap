package dev.road.map.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.domain.user.User;

public interface FriendRepository extends JpaRepository<Friend, Long>{
	
	// 본인 신청(본인이 친구 요청, 본인이 user1 = 본인이 user_send_friend), 수락여부 true, 삭제여부 false 
	List<Friend> findByUser1AndAcceptAndIsdelete(User user, Boolean accept, Boolean isDelete);

	// 본인 수신(상대방이 친구 요청, 본인이 user2 = 본인이 user_recieve_friend), 수락여부 true, 삭제여부 false 
	List<Friend> findByUser2AndAcceptAndIsdelete(User user, Boolean accept, Boolean isDelete);

	// 본인신청 & 삭제 null & 수락 y + 본인 수신 & 삭제 null & 수락 y
	// 친구 리스트 조회(user 리턴)
//	@Query(value=
//			// 본인 신청 
//			"select f.email_f1 "
//			+ "from user AS u JOIN friend AS f "
//			+ "ON u.email= f.email_f1 "
//			+ "where f.email_f1=?1 " // 입력받은 email
//			+ "AND f.accept=TRUE "
//			+ "AND NOT f.isdelete=TRUE "
//			+ "AND NOT u.isdelete=TRUE "
//			+ "UNION ALL "
//			// 본인 수신
//			+ "select f.email_f2 "
//			+ "from user AS u JOIN friend AS f "
//			+ "ON u.email= f.email_f2 "
//			+ "where f.email_f2=?1 " // 입력받은 email
//			+ "AND f.accept=TRUE "
//			+ "AND NOT f.isdelete=TRUE "
//			+ "AND NOT u.isdelete=TRUE"
//			, nativeQuery = true) 
//	public List<Friend> selectAllFriends(User user1);

	// 다른 정원 둘러보기 리스트(랜덤 매칭)(추후구현) 
	
	
	// 사용용도: insert: 친구 신청, update: 수락 or 거절(), 친구 끊기
//	public Friend save(Friend friend);
//	
//	// 받은 친구 신청 리스트(본인 수신 & 삭제 null & 수락 null)
//	@Query(value = 
//			"SELECT email_f1 FROM friend "
//			+ "WHERE email_f2=?1 "
//			+ "AND NOT isdelete=TRUE "
//			+ "AND accept=null" , nativeQuery = true) 
//	public List<Friend> selectAllPropsalFrom(User user2); // String email인지 확인 필요
//	
//	// 정원사 검색(닉네임 기반)
//	@Query(value = 
//			"SELECT * FROM user "
//			+ "WHERE nickname=?1 "
//			+ "AND NOT isdelete=TRUE "
//			+ "AND NOT unmatching=TRUE", nativeQuery = true)
//	public User search(String nickname);


}
