package org.example.cinemapjt.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterSeatDto {
    private String theaterId;
    private String rowNumber;
    private String seatNumber;
    private LocalDateTime showTime;
    private boolean isReserved;
    private String movieId;
}
