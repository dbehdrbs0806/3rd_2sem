package org.example.cinemapjt.service;

import lombok.*;
import org.example.cinemapjt.domain.dao.ReservationSeatDao;
import org.example.cinemapjt.domain.dto.DiscountDto;
import org.example.cinemapjt.domain.dto.ReservationSeatDto;
import org.example.cinemapjt.domain.dto.TheaterSeatDto;
import org.example.cinemapjt.domain.entity.Discount;
import org.example.cinemapjt.domain.entity.Member;
import org.example.cinemapjt.domain.repository.DiscountRepository;
import org.example.cinemapjt.domain.repository.MemberRepository;
import org.example.cinemapjt.domain.repository.ReservationSeatRepository;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationSeatService {

    private final MemberRepository memberRepository;
    private final DiscountRepository discountRepository;

    private final ReservationSeatDao reservationSeatDao;


    public void saveAllReservationsWithDiscount(List<ReservationSeatDto> reservationSeatDtos) {
        for (ReservationSeatDto reservationSeatDto : reservationSeatDtos) {
            saveReservationWithDiscount(reservationSeatDto); // 기존 메서드 호출
        }
    }


    public void saveReservationWithDiscount(ReservationSeatDto reservationSeatDto) {
        // 1. 회원번호로 회원 정보 조회
        Member member = memberRepository.findByMemberId(reservationSeatDto.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }

        // 2. 회원 등급으로 할인율 조회
        Discount discount = discountRepository.findByGrade(member.getGrade());
        if (discount == null) {
            throw new IllegalArgumentException("할인율 정보를 찾을 수 없습니다.");
        }

        // 할인율 변환 (필요시)
        double discountRate = discount.getDiscountRate();
        if (discountRate > 1) { // 백분율 값인지 확인
            discountRate = discountRate / 100.0; // 소수점으로 변환
        }

        // 3. 금액 계산 (문자열 금액 -> 숫자로 변환 후 할인율 적용)
        int originalAmount = Integer.parseInt(reservationSeatDto.getAmount());
        int discountedAmount = (int) (originalAmount * (1 - discountRate));

        // 4. DTO 업데이트
        reservationSeatDto.setAmount(String.valueOf(discountedAmount)); // 할인된 금액 반영

        // 5. DAO를 통해 데이터 저장
        reservationSeatDao.saveReservationSeat(reservationSeatDto);
    }

    public List<ReservationSeatDto> findReservations(String movieId, String showTime, String theaterId, String memberId) {

        // DAO에서 데이터를 조회
        return reservationSeatDao.findReservationsByCondition(movieId, showTime, theaterId, memberId);
    }



    /**
     * 예약 데이터 삭제
     *
     * @param movieId   영화 번호
     * @param showTime  상영 시간
     * @param theaterId 상영관 번호
     * @param memberId  회원 번호
     * @return 삭제 여부
     */
    public boolean deleteReservation(String movieId, LocalDateTime showTime, String theaterId, String memberId) {
        int rowsAffected = reservationSeatDao.deleteReservationByCondition(movieId, showTime, theaterId, memberId);
        return rowsAffected > 0; // 삭제된 행이 1개 이상이면 true 반환
    }



    /*public boolean updateReservationStatus(String theaterId, String rowNumber, String seatNumber, LocalDateTime showTime, String movieId) {
        return reservationSeatDao.updateSeatStatus(theaterId, rowNumber, seatNumber, showTime, movieId) > 0;
    }*/

}
