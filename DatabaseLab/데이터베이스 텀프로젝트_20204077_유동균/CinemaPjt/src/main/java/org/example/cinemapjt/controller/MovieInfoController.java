package org.example.cinemapjt.controller;


import org.example.cinemapjt.domain.dto.MovieDto;
import org.example.cinemapjt.domain.dto.ScheduleDto;
import org.example.cinemapjt.service.MovieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MovieInfoController {

    private final MovieInfoService movieInfoService;

    @Autowired
    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }


    @GetMapping(value="/movies")
    public String showMoviesPage() { return "movies"; }      // movies.html, 로그인 후 영화 선택 페이지 렌더링


    @GetMapping("/movies/info/{movieId}")
    public String showMovieInfoPage(@PathVariable String movieId, Model model) {
        // 영화 상세 정보 가져오기
        Optional<MovieDto> movieOptional = movieInfoService.getMovieById(movieId);
        if (movieOptional.isPresent()) {
                model.addAttribute("movie", movieOptional.get());

            // 상영 시간표 가져오기
            List<ScheduleDto> schedules = movieInfoService.getSchedulesByMovieId(movieId);
            model.addAttribute("schedules", schedules);

            // movie-info.html 뷰로 이동
            return "movies_info";
        } else {
            // 영화가 존재하지 않을 경우 에러 페이지 또는 다른 처리
            return "redirect:/movies";
        }
    }

}

