package org.example.cinemapjt.controller;

import jakarta.servlet.http.HttpSession;
import org.example.cinemapjt.domain.dto.MemberDto;
import org.example.cinemapjt.domain.entity.Member;
import org.example.cinemapjt.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody MemberDto memberDto) {
        try {
            memberService.registerMember(memberDto);
            return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        String memberId = loginData.get("memberId");
        String password = loginData.get("password");

        try {
            Member member = memberService.login(memberId, password);
            session.setAttribute("loggedInMemberId", member.getMemberId()); // 세션에 memberId 저장
            return ResponseEntity.ok(Map.of("message", "로그인 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    // 회원정보 조회 API (세션에서 아이디를 가져오는 대신 매개변수 활용)
    @GetMapping("/details")
    public ResponseEntity<MemberDto> getMemberDetails(HttpSession session) {
        String memberId = (String) session.getAttribute("loggedInMemberId"); // 세션에서 memberId 가져오기
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 세션에 값이 없으면 401 반환
        }

        try {
            MemberDto member = memberService.getMemberDetails(memberId); // memberId로 DB 조회
            return ResponseEntity.ok(member);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    // 회원 정보 수정
    @PostMapping("/update")
    public ResponseEntity<String> updateMember(@RequestBody MemberDto memberDto, HttpSession session) {
        String loggedInMemberId = (String) session.getAttribute("loggedInMemberId");
        if (loggedInMemberId == null || !loggedInMemberId.equals(memberDto.getMemberId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원 정보 수정 권한이 없습니다.");
        }
        try {
            memberService.updateMemberInfo(memberDto);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
