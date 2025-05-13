package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/*
ReservationSeat에서 사용되는 외부키의 모음 테이블
ReservationSeatId
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReservationSeatId implements Serializable {

    @Column(name = "영화번호", length = 200, nullable = false)
    private String movieId;

    @Column(name = "상영시간", nullable = false)
    private LocalDateTime showTime;

    @Column(name = "상영관번호", length = 10, nullable = false)
    private String theaterId;

    @Column(name = "열번호", length = 5, nullable = false)
    private String rowNumber;

    @Column(name = "위치번호", length = 5, nullable = false)
    private String seatNumber;

}
