package org.example.cinemapjt.domain.dao;


import lombok.RequiredArgsConstructor;
import org.example.cinemapjt.domain.dto.ReservationSeatDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationSeatDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 예매 좌석 데이터를 데이터베이스에 저장합니다.
     * @param reservationSeatDto 예매 데이터를 담은 DTO
     */
    public void saveReservationSeat(ReservationSeatDto reservationSeatDto) {
        String sql = "INSERT INTO 예매좌석 (영화번호, 상영시간, 상영관번호, 열번호, 위치번호, 회원번호, 금액, 인원수) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


        jdbcTemplate.update(sql,
                reservationSeatDto.getMovieId(),
                reservationSeatDto.getShowTime(),
                reservationSeatDto.getTheaterId(),
                reservationSeatDto.getRowNumber(),
                reservationSeatDto.getSeatNumber(),
                reservationSeatDto.getMemberId(),
                reservationSeatDto.getAmount(),
                reservationSeatDto.getPersonCount()); // 인원수 추가
    }

    /**
     * 특정 조건에 맞는 예약 좌석 데이터를 조회합니다.
     * @param movieId   영화 번호
     * @param showTime  상영 시간
     * @param theaterId 상영관 번호
     * @return 예약 좌석 정보 목록
     */
    public List<ReservationSeatDto> findReservationsByCondition(String movieId, String showTime, String theaterId, String memberId) {
        String sql = "SELECT * FROM 예매좌석 " +
                "WHERE 영화번호 = ? AND 상영시간 = ? AND 상영관번호 = ? AND 회원번호 = ?";

        String formattedShowTime = showTime.replace("T", " ");

        return jdbcTemplate.query(sql, new Object[]{
                movieId,
                Timestamp.valueOf(formattedShowTime), // LocalDateTime -> Timestamp 변환
                theaterId,
                memberId
        }, (rs, rowNum) -> {
            ReservationSeatDto dto = new ReservationSeatDto();
            dto.setMovieId(rs.getString("영화번호"));
            dto.setShowTime(rs.getTimestamp("상영시간").toLocalDateTime()); // Timestamp -> LocalDateTime 변환
            dto.setTheaterId(rs.getString("상영관번호"));
            dto.setRowNumber(rs.getString("열번호"));
            dto.setSeatNumber(rs.getString("위치번호"));
            dto.setMemberId(rs.getString("회원번호"));
            dto.setMemberId(rs.getString("회원번호"));
            dto.setAmount(rs.getString("금액")); // 금액은 문자열 그대로 사용
            dto.setPersonCount(rs.getString("인원수")); // 인원수도 문자열로 저장
            return dto;
        });
    }


    /**
     * 조건에 맞는 예약 데이터를 삭제
     *
     * @param movieId   영화 번호
     * @param showTime  상영 시간
     * @param theaterId 상영관 번호
     * @param memberId  회원 번호
     * @return 삭제된 행의 개수
     */
    public int deleteReservationByCondition(String movieId, LocalDateTime showTime, String theaterId, String memberId) {
        String sql = "DELETE FROM 예매좌석 " +
                "WHERE 영화번호 = ? AND 상영시간 = ? AND 상영관번호 = ? AND 회원번호 = ?";

        // LocalDateTime -> String 변환 (초 단위로 포맷)
        String formattedShowTime = showTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Timestamp timestamp = Timestamp.valueOf(showTime);
        // String formattedShowTime = showTime.replace("T", " ");
        System.out.println("SQL 쿼리 실행:");
        System.out.println("SQL: " + sql);
        System.out.println("파라미터:");
        System.out.println("movieId: " + movieId);
        System.out.println("showTime (Timestamp): " + formattedShowTime);
        System.out.println("theaterId: " + theaterId);
        System.out.println("memberId: " + memberId);

        return jdbcTemplate.update(sql, movieId, formattedShowTime, theaterId, memberId);
    }


   /* public int updateSeatStatus(String theaterId, String rowNumber, String seatNumber, LocalDateTime showTime, String movieId) {
        String selectSql = "SELECT 예약유무 FROM 상영관좌석 " +
                "WHERE 상영관번호 = ? AND 열번호 = ? AND 위치번호 = ? AND 상영시간 = ? AND 영화번호 = ?";

        // LocalDateTime -> Timestamp 변환
        Timestamp timestamp = Timestamp.valueOf(showTime);

        // 현재 예약유무 상태 조회
        Integer currentStatus = jdbcTemplate.queryForObject(selectSql, Integer.class, theaterId, rowNumber, seatNumber, timestamp, movieId);
        if (currentStatus == null) {
            throw new IllegalStateException("좌석 정보를 찾을 수 없습니다.");
        }

        // 상태 반전: 0 -> 1, 1 -> 0
        int newStatus = (currentStatus == 0) ? 1 : 0;

        // 상태를 업데이트하는 SQL
        String updateSql = "UPDATE 상영관좌석 " +
                "SET 예약유무 = ? " +
                "WHERE 상영관번호 = ? AND 열번호 = ? AND 위치번호 = ? AND 상영시간 = ? AND 영화번호 = ?";

        System.out.println("SQL 쿼리 실행:");
        System.out.println("SELECT SQL: " + selectSql);
        System.out.println("UPDATE SQL: " + updateSql);
        System.out.println("파라미터:");
        System.out.println("theaterId: " + theaterId);
        System.out.println("rowNumber: " + rowNumber);
        System.out.println("seatNumber: " + seatNumber);
        System.out.println("showTime (Timestamp): " + timestamp);
        System.out.println("movieId: " + movieId);
        System.out.println("newStatus: " + newStatus);

        // 업데이트 실행
        return jdbcTemplate.update(updateSql, newStatus, theaterId, rowNumber, seatNumber, timestamp, movieId);
        }
*/


}
