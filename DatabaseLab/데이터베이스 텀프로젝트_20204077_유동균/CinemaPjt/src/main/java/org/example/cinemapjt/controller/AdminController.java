package org.example.cinemapjt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {



    /**
     * 회원 승인 관리 페이지
     * @return approve_users.html 렌더링
     */
    @GetMapping("/approve_users")
    public String showApproveUsersPage() {
        return "approve_users"; // View 템플릿 렌더링
    }

    /**
     * 할인율 관리 페이지
     * @return manage_discounts.html 렌더링
     */
    @GetMapping("/manage_discounts")
    public String showManageDiscountsPage() {
        return "manage_discounts"; // View 템플릿 렌더링
    }

    /**
     * 영화 등록 및 상영시간표 작성 페이지
     * @return manage_movies.html 렌더링
     */
    @GetMapping("/manage_movies")
    public String showManageMoviesPage() {
        return "manage_movies"; // View 템플릿 렌더링
    }
}
