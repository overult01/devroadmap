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

import dev.road.map.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "oauthid", insertable=false, updatable=false) // oauthid 컬럼이 pk, fk 관계 
    private Member me;
	
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "oauthid", insertable=false, updatable=false) // oauthid 컬럼이 pk, fk 관계 
    private Member you;
	
    private String message;
	
    @CreationTimestamp
	private Timestamp writedate;

    @Column(nullable = false)
    private Boolean isdelete;
}
