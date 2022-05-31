package dev.road.map.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dev.road.map.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "oauthid", insertable=false, updatable=false) // oauthid 컬럼이 pk, fk 관계 
	private Member oauthid_f1;
	
	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩
	@JoinColumn(name = "oauthid", insertable=false, updatable=false) // oauthid 컬럼이 pk, fk 관계 
    private Member oauthid_f2;

	private Boolean accept;
    private LocalDateTime frienddate;
    private Boolean isdelete;
}
