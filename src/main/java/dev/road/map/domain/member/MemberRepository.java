package dev.road.map.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>{

    // 조회 
    public Member findByOauthId(String oauthId);
//    {
//    	return em.find(Member.class, oauthid);
//    }
    
    // 닉네임 조회
    Member findByNickname(String nickname);    
    
    // 저장
//    public void save(Member member);
//    {
//        em.persist(member);
//    }
}