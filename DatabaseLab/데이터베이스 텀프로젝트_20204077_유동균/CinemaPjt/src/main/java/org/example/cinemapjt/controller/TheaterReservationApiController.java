package org.example.cinemapjt.controller;


import org.apache.logging.log4j.Logger;
import org.example.cinemapjt.domain.dto.TheaterSeatDto;
import org.example.cinemapjt.domain.entity.Schedule;
import org.example.cinemapjt.service.ScheduleService;
import org.example.cinemapjt.service.TheaterSeatService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class TheaterReservationApiController {

    private final TheaterSeatService theaterSeatService;
    private final ScheduleService scheduleService;


    @Autowired
    public TheaterReservationApiController(TheaterSeatService theaterSeatService, ScheduleService scheduleService) {
        this.theaterSeatService = theaterSeatService;
        this.scheduleService = scheduleService;
    }

    /**
     * 특정 상영스케줄의 좌석 데이터 조회
     */

    @GetMapping("/seats")
    public ResponseEntity<List<TheaterSeatDto>> getSeats(
            @RequestParam("movieId") String movieId,
            @RequestParam("showTime") String showTime,
            @RequestParam("theaterId") String theaterId) {

        LocalDateTime ch_showTime;
        try {
            ch_showTime = LocalDateTime.parse(showTime, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid showTime format: " + showTime);
            return ResponseEntity.badRequest().build(); // 잘못된 형식 반환
        }

        // Service를 통해 좌석 데이터 조회
        List<TheaterSeatDto> seats = theaterSeatService.getSeatsBySchedule(movieId, ch_showTime, theaterId);

        if (seats.isEmpty()) {
            System.out.println("No seat data found for the given schedule.");
            return ResponseEntity.noContent().build(); // 좌석 데이터가 없을 경우
        }
        return ResponseEntity.ok(seats); // 좌석 데이터 반환
    }


   /*// 좌석 요금 계산을 위해 상영영화의 가격을 가져오는 url경로
    @GetMapping("/schedule")
    public ResponseEntity<?> getSchedule(
            @RequestParam("movieId") String movieId,
            @RequestParam("showTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showTime,
            @RequestParam("theaterId") String theaterId) {
        // TODO: 서비스 또는 리포지토리에서 스케줄 정보를 가져옵니다.
        Schedule schedule = scheduleService.findSchedule(movieId, showTime, theaterId);

        if (schedule == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 스케줄 정보를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(schedule);
    }*/
   // 좌석 요금 계산을 위해 상영영화의 가격을 가져오는 URL 경로
   @GetMapping("/schedule")
   public ResponseEntity<?> getSchedule(
           @RequestParam("movieId") String movieId,
           @RequestParam("showTime") String showTimeString, // 문자열로 받음
           @RequestParam("theaterId") String theaterId) {

       // 문자열 -> LocalDateTime 변환
       LocalDateTime showTime;
       try {
           showTime = LocalDateTime.parse(showTimeString, DateTimeFormatter.ISO_DATE_TIME);
       } catch (DateTimeParseException e) {
           System.out.println("Invalid showTime format: " + showTimeString);
           return ResponseEntity.badRequest()
                   .body("잘못된 showTime 형식입니다. ISO-8601 형식(예: 2024-12-02T18:50:00)으로 전달하세요.");
       }

       // 스케줄 정보를 조회
       Schedule schedule = scheduleService.findSchedule(movieId, showTime, theaterId);

       if (schedule == null) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body("해당 스케줄 정보를 찾을 수 없습니다.");
       }

       return ResponseEntity.ok(schedule);
   }

}
