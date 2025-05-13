package org.example.cinemapjt.service;


import org.example.cinemapjt.domain.dao.TheaterSeatDao;
import org.example.cinemapjt.domain.dto.TheaterSeatDto;
import org.example.cinemapjt.domain.entity.ScheduleId;
import org.example.cinemapjt.domain.entity.TheaterSeat;
import org.example.cinemapjt.domain.repository.TheaterRepository;
import org.example.cinemapjt.domain.repository.TheaterSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TheaterSeatService {
    private final TheaterSeatRepository theaterSeatRepository;
    private final TheaterSeatDao theaterSeatDao;

    @Autowired
    public TheaterSeatService(TheaterSeatRepository theaterSeatRepository, TheaterSeatDao theaterSeatDao) {
        this.theaterSeatRepository = theaterSeatRepository;

        this.theaterSeatDao = theaterSeatDao;
    }
    /**
     * 특정 상영스케줄의 좌석 조회
     */
    /**
     * 특정 상영스케줄의 좌석 조회
     */
    public List<TheaterSeatDto> getSeatsBySchedule(String movieId, LocalDateTime showTime, String theaterId) {
        // DAO를 통해 좌석 데이터를 가져오는 메서드 호출
        List<Map<String, Object>> seatDataList = theaterSeatDao.findSeatsBySchedule(movieId, Timestamp.valueOf(showTime), theaterId);

        // Map 데이터를 DTO로 변환
        return seatDataList.stream()
                .map(data -> new TheaterSeatDto(
                        (String) data.get("상영관번호"),       // theaterId
                        (String) data.get("열번호"),          // rowNumber
                        (String) data.get("위치번호"),        // seatNumber
                        showTime,                            // showTime (이미 인자로 전달받음)
                        ((boolean) data.get("예약유무")), // isReserved (1 -> true, 0 -> false)
                        movieId                              // movieId (이미 인자로 전달받음)
                ))
                .collect(Collectors.toList());
    }





    public boolean updateSeatStatuses(List<TheaterSeatDto> seatUpdates) {
        boolean allUpdated = true;

        for (TheaterSeatDto seat : seatUpdates) {
            try {
                // 각 좌석의 예약 상태를 반전
                int rowsAffected = theaterSeatDao.updateSeatStatus(
                        seat.getTheaterId(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        seat.getShowTime(),
                        seat.getMovieId()
                );

                if (rowsAffected == 0) {
                    allUpdated = false;
                    System.out.println("[Service] 좌석 상태 업데이트 실패: " + seat);
                }
            } catch (Exception e) {
                allUpdated = false;
                System.out.println("[Service] 좌석 상태 업데이트 중 예외 발생: " + seat);
                e.printStackTrace();
            }
        }

        return allUpdated; // 모든 업데이트 성공 시 true 반환
    }



}
