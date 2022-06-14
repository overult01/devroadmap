package dev.road.map.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import dev.road.map.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter @Setter
public class History {

	// 사용자별 완료 과목 표시 (사용자 중복 가능, 완료한 과목마다 insert)
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "user_email", insertable=false, updatable=false) // userid 컬럼이 pk, fk 관계 
    private User user;

	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "subject_id", insertable=false, updatable=false) // subjects_id 컬럼이 pk, fk 관계 	
    private Subject subject;
    
	@CreationTimestamp
	private Timestamp completedate;    
    
}
