package org.example.cinemapjt.controller;

import org.example.cinemapjt.domain.dto.MovieDto;
import org.example.cinemapjt.domain.dto.ScheduleDto;
import org.example.cinemapjt.service.MovieInfoService;
import org.example.cinemapjt.service.MovieScheduleService;
import org.example.cinemapjt.service.MovieUploadService;
import org.example.cinemapjt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieApiController {

    private final MovieScheduleService movieService;
    private final ScheduleService scheduleService;

    private final MovieInfoService movieInfoService;

    @Autowired
    public MovieApiController(MovieScheduleService movieService, ScheduleService scheduleService, MovieInfoService movieInfoService) {
        this.movieService = movieService;
        this.scheduleService = scheduleService;
        this.movieInfoService = movieInfoService;
    }

    /*
    영화 선택 페이지에서 상영스케줄에 저장된 영화를 불러오기 위한 API
     */
    @GetMapping("/scheduled")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        // scheduleService서비스에서 getAllSchedules()로 상영스케줄 데이터 가져옴
        List<ScheduleDto> schedules = scheduleService.getAllSchedules();
        // 상영 스케줄에서 영화 ID 추출
        List<String> movieIds = schedules.stream()
                .map(ScheduleDto::getMovieId)
                .distinct() // 상영 스케줄에는 같은 영화가 시간이 다른거라 중복 제거 필요
                .collect(Collectors.toList());

        // 영화 ID로 영화 정보 조회
        List<MovieDto> movies = movieService.getAllMoviesByIds(movieIds);

        return ResponseEntity.ok(movies);
    }

    /*
    영화 선택 페이지에서 상영스케줄에 저장되지 않은 상영 예정작 영화를 불러오기 위한 API
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Map<String, List<MovieDto>>> getUpcomingMoviesByGenre() {
        // 서비스 계층에서 상영 예정 영화를 장르별로 그룹화하여 가져옴
        Map<String, List<MovieDto>> moviesByGenre = movieService.getUpcomingMoviesByGenre();

        return ResponseEntity.ok(moviesByGenre);
    }

    /*
    상세 영화 선택 후 예매 사이트로 이동
     */
    @GetMapping("/information/{movieId}")
    public ResponseEntity<MovieDto> getMovieInfo(@PathVariable String movieId) {
        Optional<MovieDto> movieOptional = movieInfoService.getMovieById(movieId);
        if (movieOptional.isPresent()) {
            System.out.println("응답 데이터: " + movieOptional.get()); // 디버깅용 로그
            return ResponseEntity.ok(movieOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /*
   특정 영화의 상영 시간표 조회 API
   - @GetMapping("/{movieId}/schedules"): 해당 영화의 상영 시간표를 반환
   */
    @GetMapping("/{movieId}/schedules")
    public ResponseEntity<List<ScheduleDto>> getMovieSchedules(@PathVariable String movieId) {
        List<ScheduleDto> schedules = movieInfoService.getSchedulesByMovieId(movieId);
        return ResponseEntity.ok(schedules);
    }

}
