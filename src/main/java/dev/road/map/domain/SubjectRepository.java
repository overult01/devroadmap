package dev.road.map.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.domain.user.Field;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
	
	// 필드(백엔드/ 프론트)별 과목 리스트 
	List<Subject> findByField(Field field);
}
