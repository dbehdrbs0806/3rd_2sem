package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "예매좌석")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationSeat {

    @EmbeddedId
    private ReservationSeatId id; // 복합키 객체
    /*
    ReservationSeatId에 겹치는 외부키 속성들 모두 정의
     */

    @Column(name = "금액", length = 10, nullable = false)
    private String amount;

    @Column(name = "회원번호", length = 10, nullable = false)
    private String memberId;

    @Column(name = "인원수", length = 10, nullable = false)
    private String personCount;
}
