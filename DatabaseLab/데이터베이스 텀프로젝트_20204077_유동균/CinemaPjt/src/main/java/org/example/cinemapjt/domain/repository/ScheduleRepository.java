package org.example.cinemapjt.domain.repository;

import org.example.cinemapjt.domain.entity.*;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleId> {

    /**
     * 특정 영화 번호로 상영 시간표 조회
     * @param movieId 영화 번호
     * @return 상영 시간표 리스트
     */
    List<Schedule> findByIdMovieId(String movieId);

    /**
     * 특정 상영관 번호로 상영 시간표 조회
     * @param theaterId 상영관 번호
     * @return 상영 시간표 리스트
     */
    List<Schedule> findByIdTheaterId(String theaterId);

    /**
     * 특정 상영 시간 범위로 상영 시간표 조회
     * @param startDate 시작 시간
     * @param endDate 종료 시간
     * @return 상영 시간표 리스트
     */
    List<Schedule> findByIdShowTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

}