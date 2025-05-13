package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "상영스케줄")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Schedule {

    @EmbeddedId
    private ScheduleId id; // 복합키 객체

    @Column(name = "요금", nullable = false)
    private String price;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TheaterSeat> theaterSeats = new ArrayList<>();
}
