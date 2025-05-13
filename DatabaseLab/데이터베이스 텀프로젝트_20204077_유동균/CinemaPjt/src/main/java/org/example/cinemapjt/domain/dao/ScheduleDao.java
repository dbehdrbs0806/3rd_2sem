package org.example.cinemapjt.domain.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.cinemapjt.domain.dto.ScheduleDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleDao {

    @PersistenceContext
    private EntityManager entityManager;

    // 상영 중인 영화의 고유 ID 목록 조회
    public List<String> findDistinctMovieIds() {
        String jpql = "SELECT DISTINCT s.id.movieId FROM Schedule s";
        return entityManager.createQuery(jpql, String.class)
                .getResultList();
    }

    // 특정 영화 ID의 스케줄 조회
    public List<ScheduleDto> findSchedulesByMovieId(String movieId) {
        String jpql = "SELECT new org.example.cinemapjt.domain.dto.ScheduleDto(" +
                "s.id.movieId, s.id.theaterId, s.id.showTime, s.price) " +
                "FROM Schedule s WHERE s.id.movieId = :movieId";
        return entityManager.createQuery(jpql, ScheduleDto.class)
                .setParameter("movieId", movieId)
                .getResultList();
    }

    // 특정 상영관에서 상영 중인 영화 조회
    public List<ScheduleDto> findSchedulesByTheaterId(String theaterId) {
        String jpql = "SELECT new org.example.cinemapjt.domain.dto.ScheduleDto(" +
                "s.id.movieId, s.id.theaterId, s.id.showTime, s.price) " +
                "FROM Schedule s WHERE s.id.theaterId = :theaterId";
        return entityManager.createQuery(jpql, ScheduleDto.class)
                .setParameter("theaterId", theaterId)
                .getResultList();
    }
}
