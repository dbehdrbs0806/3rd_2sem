package org.example.cinemapjt.domain.repository;


import org.example.cinemapjt.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByMemberId(String memberId);

}

