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
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    // 본인신청(친구로 신청한 회원) 
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "userid_f1", insertable=false, updatable=false) // userid 컬럼이 pk, fk 관계 
	private User user1; 
	
	// 본인 수락(친구 신청을 받은 회원)
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "userid_f2", insertable=false, updatable=false) // userid 컬럼이 pk, fk 관계 
	private User user2;

	@CreationTimestamp
	private Timestamp frienddate;

    private Boolean accept;
    private Boolean isdelete;
}
