package org.example.cinemapjt.domain.repository;

import org.example.cinemapjt.domain.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, String> {

    // 상영관 타입으로 상영관 조회
    List<Theater> findByTheaterType(String theaterType);

    // 특정 상영관 번호로 상영관 존재 여부 확인
    boolean existsByTheaterId(String theaterId);
}
