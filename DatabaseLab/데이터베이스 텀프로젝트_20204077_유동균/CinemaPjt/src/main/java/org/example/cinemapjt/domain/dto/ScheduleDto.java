package org.example.cinemapjt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private String movieId;      // 영화 번호
    private String theaterId;    // 상영관 번호
    private LocalDateTime showTime; // 상영 시간
    private String price;        // 상영 요금
}