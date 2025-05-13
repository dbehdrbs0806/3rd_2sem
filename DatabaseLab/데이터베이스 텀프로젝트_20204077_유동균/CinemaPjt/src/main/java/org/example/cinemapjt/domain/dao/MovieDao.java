package org.example.cinemapjt.domain.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.cinemapjt.domain.dto.MovieDto;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public class MovieDao {

    @PersistenceContext
    private EntityManager entityManager;

    // 상영 스케줄에 없는 영화 조회
    public List<MovieDto> findMoviesNotInSchedule(List<String> scheduledMovieIds) {
        String jpql = "SELECT new org.example.cinemapjt.domain.dto.MovieDto(" +
                "m.movieId, m.title, m.genre, m.director, m.leadActor, m.posterPath) " +
                "FROM Movie m WHERE m.movieId NOT IN :scheduledMovieIds";


        List<MovieDto> movies = entityManager.createQuery(jpql, MovieDto.class)
                .setParameter("scheduledMovieIds", scheduledMovieIds)
                .getResultList();

        // posterPath에 "images/" 경로 추가
        movies.forEach(movie -> movie.setPosterPath("images/" + movie.getPosterPath()));

        return movies;
    }

    // 특정 장르의 영화 조회
    public List<MovieDto> findMoviesByGenre(String genre) {
        String jpql = "SELECT new org.example.cinemapjt.domain.dto.MovieDto(" +
                "m.movieId, m.title, m.genre, m.director, m.leadActor, m.posterPath) " +
                "FROM Movie m WHERE m.genre = :genre";
        return entityManager.createQuery(jpql, MovieDto.class)
                .setParameter("genre", genre)
                .getResultList();
    }

}
