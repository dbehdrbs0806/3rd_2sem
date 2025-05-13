package org.example.cinemapjt.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


// model을 통해 html반환과 데이터를 함께 넣을 수 있음
// thymeleaf 문법
@Controller
public class TheaterReservationController {

   /* *//**
     * 좌석 선택 페이지 반환
     */

    @GetMapping("/seat-selection")
    public String showSeatSelectionPage(
            @RequestParam("movieId") String movieId,
            @RequestParam("showTime") String showTime,
            @RequestParam("theaterId") String theaterId,
            Model model) {

        System.out.println("movieId: " + movieId); // 값 출력
        System.out.println("showTime: " + showTime);
        System.out.println("theaterId: " + theaterId);

       /* DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String showTimeString = showTime.format(formatter);
*/
        // 필요한 데이터를 모델에 추가하여 뷰에 전달
        model.addAttribute("movieId", movieId);
        model.addAttribute("showTime", showTime);
        model.addAttribute("theaterId", theaterId);

        // theater_reservation.html 파일을 반환
        return "theater_reservation";
    }

    /*@GetMapping("/seat-selection")
    public String showSeatSelectionPage() {
        // theater_reservation.html 파일 반환
        return "theater_reservation";
    }*/
}
