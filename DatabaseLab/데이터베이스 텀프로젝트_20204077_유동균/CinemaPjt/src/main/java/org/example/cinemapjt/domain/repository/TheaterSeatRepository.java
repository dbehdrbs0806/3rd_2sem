package org.example.cinemapjt.domain.repository;

import org.example.cinemapjt.domain.entity.ScheduleId;
import org.example.cinemapjt.domain.entity.TheaterSeat;
import org.example.cinemapjt.domain.entity.TheaterSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {
    List<TheaterSeat> findByScheduleId(ScheduleId scheduleId);
}

