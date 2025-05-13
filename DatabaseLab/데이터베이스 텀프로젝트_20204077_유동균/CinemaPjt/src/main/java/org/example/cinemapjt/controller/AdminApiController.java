package org.example.cinemapjt.controller;

import org.example.cinemapjt.domain.dto.*;
import org.example.cinemapjt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/*
AdminApiController
관리자(Admin) 페이지에서 데이터 호출과 URL 매핑을 처리하는 Controller
- @RestController: JSON, XML 등 데이터를 응답으로 처리할 수 있는 어노테이션
- @Autowired: 의존성 주입(DI) 설정을 위한 어노테이션
- @RequestMapping: URL 주소 매핑을 설정하는 어노테이션
*/
@RestController
@RequestMapping("/admin/api")
public class AdminApiController {

    // 의존성 주입을 위한 서비스 객체 선언
    private final MemberService memberService;
    private final GradeService gradeService;
    private final MovieUploadService movieService;
    private final ScheduleService scheduleService;

    /* 포스터 상대 경로 상수 */
    private static final String POSTER_BASE_PATH = "/images/";

    /*
    생성자 방식의 의존성 주입 (DI)
    @param memberService 회원 관련 서비스
    @param gradeService 등급 및 할인율 관련 서비스
    @param movieService 영화 등록 및 관리 서비스
    @param scheduleService 상영 시간표 관리 서비스
    */
    @Autowired
    public AdminApiController(MemberService memberService, GradeService gradeService,
                              MovieUploadService movieService, ScheduleService scheduleService) {
        this.memberService = memberService;
        this.gradeService = gradeService;
        this.movieService = movieService;
        this.scheduleService = scheduleService;
    }

    /*
    회원 정보 조회 API
    - @GetMapping("/users"): 모든 회원 정보를 반환
    - memberService의 getAllMembers() 호출
    */
    @GetMapping("/users")
    public ResponseEntity<List<MemberDto>> getAllUsers() {
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    /*
    회원 정보 업데이트 API
    - @PostMapping("/users/update"): 회원 정보를 업데이트
    - 요청 JSON 데이터를 @RequestBody로 처리
    */
    @PostMapping("/users/update")
    public ResponseEntity<String> updateUserStatus(@RequestBody MemberDto memberDto) {
        try {
            memberService.updateMemberStatus(memberDto);
            if (memberDto.getGrade() != null) {
                return ResponseEntity.ok("회원 등급이 성공적으로 업데이트되었습니다.");
            }
            return ResponseEntity.ok("회원 정보가 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    할인율 정보 조회 API
    - @GetMapping("/discounts"): 모든 할인율 정보를 반환
    - gradeService의 getAllDiscounts() 호출
    */
    @GetMapping("/discounts")
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        try {
            List<DiscountDto> discounts = gradeService.getAllDiscounts();
            return ResponseEntity.ok(discounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
    특정 등급 할인율 조회 API
    - @GetMapping("/discounts/{grade}"): 특정 등급의 할인율 조회
    - @PathVariable: URL의 동적 변수 처리 (VIP, SILVER, GOLD 등)
    */
    @GetMapping("/discounts/{grade}")
    public ResponseEntity<DiscountDto> getDiscountByGrade(@PathVariable String grade) {
        try {
            DiscountDto discount = gradeService.getDiscountByGrade(grade);
            return ResponseEntity.ok(discount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /*
    할인율 업데이트 API
    - @PostMapping("/discounts/update"): 할인율 정보를 업데이트
    - 요청 데이터를 JSON 형식으로 처리
    */
    @PostMapping("/discounts/update")
    public ResponseEntity<String> updateDiscounts(@RequestBody Map<String, Double> discountUpdates) {
        try {
            gradeService.updateDiscounts(discountUpdates);
            return ResponseEntity.ok("할인율이 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 할인율 업데이트에 실패했습니다.");
        }
    }

    /*
    영화 등록 API
    - @PostMapping("/add_movie"): 영화 정보를 등록
    - @RequestParam: 요청 데이터를 개별적으로 받아 처리
    */
    @PostMapping("/add_movie")
    public ResponseEntity<String> addMovie(
            @RequestParam("title") String title,
            @RequestParam("movieId") String movieId,
            @RequestParam("genre") String genre,
            @RequestParam("director") String director,
            @RequestParam("leadActor") String leadActor,
            @RequestParam("poster") MultipartFile poster) {
        try {
            movieService.addMovie(movieId, title, genre, director, leadActor, poster);
            return ResponseEntity.ok("영화가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("영화 등록에 실패했습니다: " + e.getMessage());
        }
    }

    /*
    영화 목록 조회 API
    - @GetMapping("/movies"): 등록된 영화 정보를 반환
    */
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getMovies() {
        try {
            List<MovieDto> movies = movieService.getAllMovies();
            movies.forEach(movie -> {
                if (movie.getPosterPath() != null) {
                    movie.setPosterPath(Paths.get(POSTER_BASE_PATH, movie.getPosterPath()).toString());
                }
            });
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
    상영 시간표 등록 API
    - @PostMapping("/add_schedule"): 상영 시간표 데이터를 등록
    - 관리자 페이지에서 상영시간표를 등록, 저장
    */
    @PostMapping("/add_schedule")
    public ResponseEntity<String> addSchedule(@RequestBody ScheduleDto scheduleDto) {
        try {
            scheduleService.addSchedule(scheduleDto);
            return ResponseEntity.ok("상영 시간표가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상영 시간표 저장에 실패했습니다.");
        }
    }

    /*
    상영 시간표 목록 조회 API
    - @GetMapping("/schedules"): 등록된 상영 시간표 데이터를 반환
    */
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleDto>> getSchedules() {
        try {
            List<ScheduleDto> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
    특정 상영 스케줄 삭제 API
    - @DeleteMapping("/delete_schedule"): 특정 상영 스케줄을 삭제
    - 요청 파라미터로 영화 번호, 상영 시간, 상영관 번호를 받음
    */
    @DeleteMapping("/delete_schedule")
    public ResponseEntity<String> deleteSchedule(@RequestBody Map<String, String> requestData) {
        try {
            // JSON 데이터에서 값 추출
            String movieId = requestData.get("movieId");
            LocalDateTime showTime = LocalDateTime.parse(requestData.get("showTime"));
            String theaterId = requestData.get("theaterId");

            // 서비스 메서드 호출하여 삭제 수행
            scheduleService.deleteSchedule(movieId, showTime, theaterId);

            return ResponseEntity.ok("상영 스케줄이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("상영 스케줄 삭제 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 상영 스케줄 삭제에 실패했습니다.");
        }
    }
}
