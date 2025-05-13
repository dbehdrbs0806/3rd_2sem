package org.example.cinemapjt.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "상영관좌석")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TheaterSeat {

    @EmbeddedId
    private TheaterSeatId id; // 복합키 객체

    @MapsId("scheduleId") // 복합 키의 부모 키인 ScheduleId와 매핑
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "영화번호", referencedColumnName = "영화번호", nullable = false),
            @JoinColumn(name = "상영시간", referencedColumnName = "상영시간", nullable = false),
            @JoinColumn(name = "상영관번호", referencedColumnName = "상영관번호", nullable = false)
    })
    @JsonIgnore // 순환 참조 방지
    private Schedule schedule;

    @Column(name = "예약유무", nullable = false)
    private Boolean isReserved = false; // 기본값: 예약되지 않음

}
