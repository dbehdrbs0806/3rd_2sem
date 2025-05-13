package org.example.cinemapjt.service;

import org.example.cinemapjt.domain.dto.MemberDto;
import org.example.cinemapjt.domain.entity.Member;
import org.example.cinemapjt.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 새로운 회원 등록
    public void registerMember(MemberDto memberDto) {
        // 아이디 중복 확인
        if (memberRepository.findByMemberId(memberDto.getMemberId()) != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // MemberDto -> Member 변환 및 기본값 설정
        Member member = new Member();
        member.setMemberId(memberDto.getMemberId());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword())); // 비밀번호 암호화
        member.setName(memberDto.getName());
        member.setPhoneNumber(memberDto.getPhoneNumber());
        member.setGrade(memberDto.getGrade() != null ? memberDto.getGrade() : "SILVER"); // 기본 등급 설정
        member.setCardNumber(memberDto.getCardNumber());
        member.setIsApproved(0); // 기본 승인 상태를 미승인으로 설정

        // 데이터 저장
        memberRepository.save(member);
    }

    // 로그인 처리
    public Member login(String memberId, String password) {
        // 회원 조회
        Member member = memberRepository.findByMemberId(memberId);

        // 회원 존재 여부 확인
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        // 승인 여부 확인
        if (member.getIsApproved() == 0) { // 승인 여부가 0이면 로그인 불가
            throw new IllegalArgumentException("관리자의 승인을 기다려주세요.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        return member; // 로그인 성공 시 Member 객체 반환
    }

    // 모든 회원 목록 조회
    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAll(); // 모든 회원 조회
        return members.stream()
                .map(this::convertToDto) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    // 회원 상태 및 등급 업데이트
    public void updateMemberStatus(MemberDto memberDto) {
        Member member = memberRepository.findByMemberId(memberDto.getMemberId());

        if (member == null) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        // 등급 및 승인 여부 업데이트
        member.setGrade(memberDto.getGrade());
        member.setIsApproved(memberDto.getIsApproved()); // 승인 여부 업데이트

        // 변경 사항 저장
        memberRepository.save(member);
    }

    // 회원정보 조회
    public MemberDto getMemberDetails(String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }
        return convertToDto(member);
    }

    // 회원정보 수정
    public void updateMemberInfo(MemberDto memberDto) {
        Member member = memberRepository.findByMemberId(memberDto.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        // 수정 가능한 정보 업데이트
        member.setPassword(passwordEncoder.encode(memberDto.getPassword())); // 비밀번호 암호화 후 저장
        member.setName(memberDto.getName());
        member.setPhoneNumber(memberDto.getPhoneNumber());
        member.setCardNumber(memberDto.getCardNumber());

        memberRepository.save(member); // 변경 내용 저장
    }


    // DTO와 ENtity를 변환할 때 사용하는 영역의 코드들
    // Entity -> DTO 변환
    private MemberDto convertToDto(Member member) {
        return new MemberDto(
                member.getMemberId(),   // 회원 ID
                member.getPassword(),   // 비밀번호
                member.getName(),       // 이름
                member.getPhoneNumber(),// 전화번호
                member.getGrade(),      // 등급
                member.getCardNumber(), // 카드 번호
                member.getIsApproved()  // 승인 여부
        );
    }

    // DTO -> Entity 변환 (필요 시 사용 가능)
    private Member convertToEntity(MemberDto memberDto) {
        Member member = new Member();
        member.setMemberId(memberDto.getMemberId());
        member.setPassword(memberDto.getPassword());
        member.setName(memberDto.getName());
        member.setPhoneNumber(memberDto.getPhoneNumber());
        member.setGrade(memberDto.getGrade());
        member.setCardNumber(memberDto.getCardNumber());
        member.setIsApproved(memberDto.getIsApproved());
        return member;
    }
}
