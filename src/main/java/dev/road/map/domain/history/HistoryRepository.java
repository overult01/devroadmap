package dev.road.map.domain.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.domain.user.User;

public interface HistoryRepository extends JpaRepository<History, Long>{
	
	// 사용자가 과목 완료 체크 표시 / 체크 해제
	History save(History history);
	
	// 유저의 완료 과목 확인 
	List<History> findByUserAndIsdelete(User user, Boolean isdelete);
	
	// 로그인한 유저의 과목별 완료여부 조회(history가 조회되면 완료상태, null이면 미완료 상태)
	History findByUserAndSubject(User user, int subject);
	
	
}
