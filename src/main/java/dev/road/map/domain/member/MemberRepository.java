package dev.road.map.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.road.map.dto.MemberDTO;

public interface MemberRepository extends JpaRepository<Member, Long>{

    // 조회 
    public Member findByOauthId(String oauthId);
    
    // 닉네임 조회
    public Member findByNickname(String nickname);    
    
}