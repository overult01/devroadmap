package dev.road.map.domain.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.domain.user.User;

public interface HistoryRepository extends JpaRepository<History, Long>{

	// 유저의 완료 과목 확인 
	List<History> findByUser(User user);
}
