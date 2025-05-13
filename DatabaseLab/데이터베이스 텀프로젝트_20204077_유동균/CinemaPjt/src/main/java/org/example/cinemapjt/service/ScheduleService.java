package org.example.cinemapjt.service;

import org.example.cinemapjt.domain.dto.ScheduleDto;
import org.example.cinemapjt.domain.entity.*;
import org.example.cinemapjt.domain.repository.ScheduleRepository;
import org.example.cinemapjt.domain.repository.TheaterSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TheaterSeatRepository theaterSeatRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, TheaterSeatRepository theaterSeatRepository) {
        this.scheduleRepository = scheduleRepository;
        this.theaterSeatRepository = theaterSeatRepository;
    }

    /**
     * 상영 시간표 추가
     *
     * @param scheduleDto 상영 시간표 DTO
     */
    public void addSchedule(ScheduleDto scheduleDto) {
        try {
            // 1. Schedule 엔티티 생성 및 저장
            Schedule schedule = toEntity(scheduleDto);
            scheduleRepository.save(schedule);
            System.out.println("Schedule saved: " + schedule);

            // 2. 상영관 형태 결정 및 좌석 생성
            String theaterType = determineTheaterType(scheduleDto.getTheaterId());
            LocalDateTime showTime = scheduleDto.getShowTime(); // DTO에서 상영 시간을 가져옴
            List<TheaterSeat> seats = generateSeats(schedule, theaterType); // 수정된 generateSeats 호출
            System.out.println("Generated seats: " + seats);

            // 3. 상영관 좌석 저장
            theaterSeatRepository.saveAll(seats);
            System.out.println("Seats saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("상영 시간표 추가 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 상영관 번호를 기반으로 상영관 형태 결정
     *
     * @param theaterId 상영관 번호
     * @return 상영관 형태 (1관, 2관 등)
     */
    private String determineTheaterType(String theaterId) {
        switch (theaterId) {
            case "1": return "1관";
            case "2": return "2관";
            case "3": return "3관";
            default: throw new IllegalArgumentException("알 수 없는 상영관 번호: " + theaterId);
        }
    }

    /**
     * 상영관 형태에 따라 좌석 생성
     *
     * @param schedule 상영 시간표
     * @param theaterType 상영관 형태
     * @return 생성된 좌석 리스트
     */
    private List<TheaterSeat> generateSeats(Schedule schedule, String theaterType) {
        List<TheaterSeat> seats = new ArrayList<>();

        switch (theaterType) {
            case "1관": // A, B, C 각 열에 3행 * 10좌석
                seats.addAll(createSeatRows(schedule, new String[]{"A", "B", "C"}, 10));
                break;

            case "2관": // A, B, C, D 각 열에 2행 * 10좌석
                seats.addAll(createSeatRows(schedule, new String[]{"A", "B", "C", "D"}, 10));
                break;

            case "3관": // A, B 각 열에 3행 * 8좌석
                seats.addAll(createSeatRows(schedule, new String[]{"A", "B"}, 8));
                break;

            default:
                throw new IllegalArgumentException("처리되지 않은 상영관 형태: " + theaterType);
        }

        return seats;
    }

    /**
     * 특정 열에 대한 좌석 생성
     *
     * @param schedule   상영 시간표
     * @param rowNumbers 열 번호 리스트 (A, B, C 등)
     * @param seatCount  열당 좌석 수
     * @return 생성된 좌석 리스트
     */
    private List<TheaterSeat> createSeatRows(Schedule schedule, String[] rowNumbers, int seatCount) {
        List<TheaterSeat> seats = new ArrayList<>();

        for (String row : rowNumbers) { // A, B, C 등 열 반복
            for (int seat = 1; seat <= seatCount; seat++) { // 좌석 번호 반복
                String seatNumber = "a" + seat; // 좌석 번호 a1, a2, ...

                TheaterSeatId seatId = new TheaterSeatId(
                        schedule.getId(),  // ScheduleId
                        row,               // 열 번호 (A, B, C 등)
                        seatNumber         // 좌석 번호 (a1, a2, ...)
                );

                TheaterSeat theaterSeat = new TheaterSeat();
                theaterSeat.setId(seatId);
                theaterSeat.setSchedule(schedule);
                theaterSeat.setIsReserved(false); // 기본값: 예약되지 않음

                seats.add(theaterSeat);
            }
        }

        return seats;
    }
    /**
     * 모든 상영 시간표 조회
     * 관리자 페이지에서 상영시간표 데이터 반환 시 사용
     *
     * @return 상영 시간표 목록 (ScheduleDto)
     */
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 시간 범위의 상영 시간표 조회
     *
     * @param startDate 시작 시간
     * @param endDate   종료 시간
     * @return 상영 시간표 목록 (ScheduleDto)
     */
    public List<ScheduleDto> getSchedulesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByIdShowTimeBetween(startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 상영 시간표 삭제
     *
     * @param movieId   영화 번호
     * @param showTime  상영 시간
     * @param theaterId 상영관 번호
     */
    public void deleteSchedule(String movieId, LocalDateTime showTime, String theaterId) {
        ScheduleId scheduleId = new ScheduleId(movieId, showTime, theaterId);
        scheduleRepository.findById(scheduleId).ifPresentOrElse(
                schedule -> scheduleRepository.deleteById(scheduleId),
                () -> {
                    throw new IllegalArgumentException("해당 상영 시간표가 존재하지 않습니다: " + scheduleId);
                });
    }

    /**
     * Schedule 엔티티를 DTO로 변환
     *
     * @param schedule Schedule 엔티티
     * @return ScheduleDto
     */
    private ScheduleDto toDto(Schedule schedule) {
        return new ScheduleDto(
                schedule.getId().getMovieId(),
                schedule.getId().getTheaterId(),
                schedule.getId().getShowTime(),
                schedule.getPrice()
        );
    }

    /**
     * ScheduleDto를 엔티티로 변환
     *
     * @param scheduleDto ScheduleDto
     * @return Schedule 엔티티
     */
    private Schedule toEntity(ScheduleDto scheduleDto) {
        ScheduleId scheduleId = new ScheduleId(
                scheduleDto.getMovieId(),
                scheduleDto.getShowTime(),
                scheduleDto.getTheaterId()
        );

        Schedule schedule = new Schedule();
        schedule.setId(scheduleId); // ScheduleId 설정
        schedule.setPrice(scheduleDto.getPrice()); // 가격 설정

        // TheaterSeats는 비어 있는 상태로 초기화 (추가 로직에서 설정)
        schedule.setTheaterSeats(new ArrayList<>());

        return schedule;
    }

    public Schedule findSchedule(String movieId, LocalDateTime showTime, String theaterId) {
        // ScheduleId 객체 생성
        ScheduleId scheduleId = new ScheduleId(movieId, showTime, theaterId);

        // Schedule 조회
        return scheduleRepository.findById(scheduleId).orElse(null);
    }
}
