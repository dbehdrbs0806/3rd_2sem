package org.example.cinemapjt.service;

import org.example.cinemapjt.domain.dao.MovieDao;
import org.example.cinemapjt.domain.dao.ScheduleDao;
import org.example.cinemapjt.domain.dto.MovieDto;
import org.example.cinemapjt.domain.repository.MovieRepository;
import org.example.cinemapjt.domain.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieScheduleService {

    // Repository 사용 선언
    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;

    // Dao 사용 선언
    private final ScheduleDao scheduleDao;
    private final MovieDao movieDao;

    @Autowired
    public MovieScheduleService(MovieRepository movieRepository, ScheduleRepository scheduleRepository, ScheduleDao scheduleDao, MovieDao movieDao) {
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleDao = scheduleDao;
        this.movieDao = movieDao;
    }

     /*
    상영중인 영화 반환해서 출력하기 위해 사용할 함수
    MovieApiController 에서 사용
     */

    // 영화 ID 리스트로 영화 정보 모두 조회

    public List<MovieDto> getAllMoviesByIds(List<String> movieIds) {
        // 영화 ID 리스트로 데이터베이스에서 조회
        return movieRepository.findAllById(movieIds).stream()
                .map(movie -> new MovieDto(
                        movie.getMovieId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getDirector(),
                        movie.getLeadActor(),
                        "images/" + movie.getPosterPath()
                ))
                .collect(Collectors.toList());
    }


    // 상영 예정 영화를 장르별로 조회
    // Dao 사용
    public Map<String, List<MovieDto>> getUpcomingMoviesByGenre() {
        // 1. 상영 중인 영화 ID 목록 조회
        List<String> scheduledMovieIds = scheduleDao.findDistinctMovieIds();

        // 2. 상영 스케줄에 없는 영화 조회
        List<MovieDto> upcomingMovies = movieDao.findMoviesNotInSchedule(scheduledMovieIds);

        // 3. 영화 데이터를 장르별로 그룹화
        return upcomingMovies.stream()
                .collect(Collectors.groupingBy(MovieDto::getGenre));
    }
}
