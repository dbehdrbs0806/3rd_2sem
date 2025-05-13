package org.example.cinemapjt.service;

import org.example.cinemapjt.domain.dto.MovieDto;
import org.example.cinemapjt.domain.dto.ScheduleDto;
import org.example.cinemapjt.domain.repository.MovieRepository;
import org.example.cinemapjt.domain.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieInfoService {

    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public MovieInfoService(MovieRepository movieRepository, ScheduleRepository scheduleRepository) {
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
    }


    /*
           특정 영화 정보를 반환
           @param movieId 영화 ID
           @return MovieDto
    */
    public Optional<MovieDto> getMovieById(String movieId) {
        return movieRepository.findById(movieId)
                .map(movie -> new MovieDto(
                        movie.getMovieId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getDirector(),
                        movie.getLeadActor(),
                        "images/" + movie.getPosterPath()
                ));
    }
    public List<ScheduleDto> getSchedulesByMovieId(String movieId) {
        return scheduleRepository.findByIdMovieId(movieId).stream()
                .map(schedule -> new ScheduleDto(
                        schedule.getId().getMovieId(),
                        schedule.getId().getTheaterId(),
                        schedule.getId().getShowTime(),
                        schedule.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
