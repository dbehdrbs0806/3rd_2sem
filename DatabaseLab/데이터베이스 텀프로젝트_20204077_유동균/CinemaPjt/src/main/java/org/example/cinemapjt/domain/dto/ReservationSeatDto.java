package org.example.cinemapjt.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSeatDto {

    private String movieId;    // 영화번호
    private LocalDateTime showTime;     // 상영시간
    private String theaterId;  // 상영관번호
    private String rowNumber;  // 열번호
    private String seatNumber; // 위치번호
    private String memberId;   // 회원번호
    private String amount;     // 금액 (문자열로 유지)
    private String personCount; // 인원수
}
