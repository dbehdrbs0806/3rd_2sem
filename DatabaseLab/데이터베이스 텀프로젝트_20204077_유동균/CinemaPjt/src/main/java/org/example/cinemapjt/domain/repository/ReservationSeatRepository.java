package org.example.cinemapjt.domain.repository;


import org.example.cinemapjt.domain.entity.ReservationSeat;
import org.example.cinemapjt.domain.entity.ReservationSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, ReservationSeatId> {
}