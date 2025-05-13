package org.example.cinemapjt.domain.repository;

import org.example.cinemapjt.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    /**
     * 영화 제목으로 영화 정보 조회
     * @param title 영화 제목
     * @return Movie 엔티티 Optional
     */
    Optional<Movie> findByTitle(String title);

    /**
     * 영화 ID로 영화 정보 조회
     * @param movieId 영화 ID
     * @return Optional<Movie>
     */
    Optional<Movie> findByMovieId(String movieId);
}