package dev.road.map.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.domain.user.User;

public interface FriendRepository extends JpaRepository<Friend, Long>{
	
	// 본인 신청(본인이 친구 요청, 본인이 user1 = 본인이 user_send_friend), 수락여부 true, 삭제여부 false 
	List<Friend> findByUser1AndAcceptAndIsdelete(User user, Boolean accept, Boolean isDelete);

	// 받은 친구 신청 리스트(본인 수신 & 삭제 null & "수락 null")
	// 본인 수신(상대방이 친구 요청, 본인이 user2 = 본인이 user_recieve_friend), 수락여부 true, 삭제여부 false 
	List<Friend> findByUser2AndAcceptAndIsdelete(User user, Boolean accept, Boolean isDelete);

	// 다른 정원 둘러보기 리스트(랜덤 매칭)(추후구현) 
	
	
	// 사용용도: insert: 친구 신청, update: 수락 or 거절(), 친구 끊기
	public Friend save(Friend friend);
	
	// 나한테 온 친구신청. 수락 or 거절()시 user1, user2로 찾기
	Friend findByUser1AndUser2(User user1, User user2);

	// 정원사 검색(닉네임 기반)
	Friend findByNicknameAndUnmatchingAndIsdelete(String search_nick, Boolean unmatching, Boolean isDelete);
}
