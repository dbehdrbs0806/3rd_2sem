package org.example.cinemapjt.domain.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class TheaterSeatDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TheaterSeatDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 좌석 상태 업데이트
     *
     * @param theaterId  상영관 ID
     * @param rowNumber  열 번호
     * @param seatNumber 좌석 번호

     * @return 변경된 행의 수
     */
    public int updateSeatStatus(String theaterId, String rowNumber, String seatNumber, LocalDateTime showTime, String movieId) {
        // 예약 상태를 가져오는 쿼리
        String selectSql = "SELECT 예약유무 FROM 상영관좌석 " +
                "WHERE 상영관번호 = ? AND 열번호 = ? AND 위치번호 = ? AND 상영시간 = ? AND 영화번호 = ?";

        // LocalDateTime -> Timestamp 변환
        Timestamp timestamp = Timestamp.valueOf(showTime);

        // 현재 예약 상태 조회
        Integer currentStatus = jdbcTemplate.queryForObject(selectSql, Integer.class, theaterId, rowNumber, seatNumber, timestamp, movieId);
        if (currentStatus == null) {
            throw new IllegalStateException("해당 좌석 정보를 찾을 수 없습니다.");
        }

        // 상태 반전: 0 -> 1, 1 -> 0
        int newStatus = (currentStatus == 0) ? 1 : 0;

        // 상태를 업데이트하는 쿼리
        String updateSql = "UPDATE 상영관좌석 " +
                "SET 예약유무 = ? " +
                "WHERE 상영관번호 = ? AND 열번호 = ? AND 위치번호 = ? AND 상영시간 = ? AND 영화번호 = ?";

        // SQL 업데이트 실행
        return jdbcTemplate.update(updateSql, newStatus, theaterId, rowNumber, seatNumber, timestamp, movieId);
    }



    public List<Map<String, Object>> findSeatsBySchedule(String movieId, Timestamp showTime, String theaterId) {
        String sql = "SELECT 상영관번호, 열번호, 위치번호, 예약유무 " +
                "FROM 상영관좌석 " +
                "WHERE 영화번호 = ? AND 상영시간 = ? AND 상영관번호 = ?";

        // SQL 실행 및 결과 반환
        return jdbcTemplate.queryForList(sql, movieId, showTime, theaterId);
    }

}
