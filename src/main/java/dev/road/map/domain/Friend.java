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
import lombok.Builder;
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
	@JoinColumn(name = "email_send_friend") // userid 컬럼이 pk, fk 관계 
	private User user1; 
	
	// 본인 수락(친구 신청을 받은 회원)
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "email_recieve_friend") // userid 컬럼이 pk, fk 관계 
	private User user2;

	@CreationTimestamp
	private Timestamp frienddate;

	// 친구 수락 여부 
    private Boolean accept;
    
    // 친구 삭제 여부
    private Boolean isdelete;
    
    @Builder
    public Friend(User user1, User user2) {
    	this.user1 = user1;
    	this.user2 = user2;
    }
}
