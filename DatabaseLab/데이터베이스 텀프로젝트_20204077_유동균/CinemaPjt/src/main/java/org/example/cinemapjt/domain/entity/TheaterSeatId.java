package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/* 외부키로 사용하기 위한 테이블의 구성
   즉 외부키로 겹쳐 사용되는 속성들이 담겨져있는 객체파일
* */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TheaterSeatId implements Serializable {

    @Embedded
    private ScheduleId scheduleId; // 부모 ScheduleId 복합 키를 포함

    @Column(name = "열번호", nullable = false, length = 10)
    private String rowNumber;

    @Column(name = "위치번호", nullable = false, length = 10)
    private String seatNumber;


}
