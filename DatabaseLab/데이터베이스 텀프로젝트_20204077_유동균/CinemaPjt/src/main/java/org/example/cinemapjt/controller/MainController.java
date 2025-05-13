package org.example.cinemapjt.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

/*
    Controller class
    Main 페이지인 index와 admin페이지 등 중요 페이지의 html 렌더링 하는 Controller
    @Controller: Controller로 사용하기 위한 어노테이션
    @GetMapping(value = "~/~/"): Get 형식 조회 형식. url의 ? 뒤의 파라이터로 내용을 받을 때 사용
 */
@Controller
public class MainController {

    @GetMapping(value = "/")
    public String showMainPage() { return "index"; }            // index.html 메인 홈페이지 렌더링

    @GetMapping(value = "/admin")
    public String showAdminPage() { return "admin"; }           // admin.html 관리자 페이지 렌더링

    @GetMapping(value = "/signup")
    public String showSignupPage() { return "signup"; }         // signup.html 회원가입 페이지 렌더링

    @GetMapping(value="/edit")
    public String showMemberEditPage() { return "member_edit"; }    // member_edit.html 회원정보 수정 페이지렌더링
}
