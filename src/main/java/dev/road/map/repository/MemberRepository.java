package dev.road.map.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import dev.road.map.domain.member.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager em;

    // 조회 
    public Member findByOauthId(String oauthid) {
    	return em.find(Member.class, oauthid);
    }
    
    // 저장
    public void save(Member member) {
        em.persist(member);
    }
}