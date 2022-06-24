package dev.road.map.domain.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.domain.user.User;

public interface HistoryRepository extends JpaRepository<History, Long>{

	// 유저의 완료 과목 확인 
	List<History> findByUser(User user);
	
	// 로그인한 유저의 과목별 완료여부 조회(history가 조회되면 완료상태, null이면 미완료 상태)
	History findByUserAndSubject(User user, Long subject);
}
