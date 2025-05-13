package org.example.cinemapjt.service;

import org.example.cinemapjt.domain.dto.MovieDto;
import org.example.cinemapjt.domain.entity.Movie;
import org.example.cinemapjt.domain.repository.MovieRepository;
import org.example.cinemapjt.domain.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieUploadService {

    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;

    @Value("${images.dir:src/main/resources/static/images/}") // 설정 파일에서 경로 읽기, 기본값 설정
    private String POSTER_DIR;

    @Autowired
    public MovieUploadService(MovieRepository movieRepository, ScheduleRepository scheduleRepository) {
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /*
    영화 정보를 추가하고 포스터 파일을 저장

    @param movieId    영화 ID
    @param title      영화 제목
    @param genre      장르
    @param director   감독
    @param leadActor  주연 배우
    @param poster     포스터 파일
    @throws IOException 파일 처리 중 오류 발생 시
    */
    public void addMovie(String movieId, String title, String genre, String director, String leadActor, MultipartFile poster) throws IOException {
        String posterPath = savePosterToFileSystem(poster);
        Movie movie = new Movie(movieId, title, genre, director, leadActor, posterPath);
        movieRepository.save(movie);
    }

    /*
    모든 영화 정보를 반환

    @return 영화 목록 (List<MovieDto>)
    */
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> new MovieDto(
                        movie.getMovieId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getDirector(),
                        movie.getLeadActor(),
                        movie.getPosterPath()
                ))
                .collect(Collectors.toList());
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
                        movie.getPosterPath()
                ));
    }

    /*
    영화와 관련된 파일 삭제

    @param movieId 삭제할 영화 ID
    @throws IOException 파일 삭제 오류 발생 시
    */
    public void deleteMovie(String movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다: " + movieId));
        if (movie.getPosterPath() != null) {
            Path filePath = Paths.get(POSTER_DIR, movie.getPosterPath().replace("images/", ""));
            Files.deleteIfExists(filePath);
        }
        movieRepository.delete(movie);
    }

    /*
    포스터 파일을 저장하고 경로를 반환

    @param poster 업로드된 포스터 파일
    @return 저장된 파일 경로
    @throws IOException 파일 처리 중 오류 발생 시
    */
    private String savePosterToFileSystem(MultipartFile poster) throws IOException {
        if (poster != null && !poster.isEmpty()) {
            if (!poster.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("업로드된 파일이 이미지 형식이 아닙니다.");
            }
            File dir = new File(POSTER_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String uniqueFileName = UUID.randomUUID().toString() + "_" + poster.getOriginalFilename();
            Path filePath = Paths.get(POSTER_DIR, uniqueFileName);
            Files.write(filePath, poster.getBytes());
            return uniqueFileName;
        }
        return null;
    }

}
