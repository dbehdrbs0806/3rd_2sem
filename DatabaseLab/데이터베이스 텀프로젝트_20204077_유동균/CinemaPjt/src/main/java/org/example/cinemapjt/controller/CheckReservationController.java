package org.example.cinemapjt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckReservationController {

    @GetMapping("/check")
    public String showCheckPage() {
        return "check_reservation";
    }
}
