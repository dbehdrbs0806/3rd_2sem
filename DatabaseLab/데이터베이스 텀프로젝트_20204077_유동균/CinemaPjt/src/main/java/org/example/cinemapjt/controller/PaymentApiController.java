package org.example.cinemapjt.controller;

import org.example.cinemapjt.domain.dto.ReservationSeatDto;
import org.example.cinemapjt.domain.dto.TheaterSeatDto;
import org.example.cinemapjt.service.ReservationSeatService;
import org.example.cinemapjt.service.TheaterSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentApiController {

    private final ReservationSeatService reservationSeatService;

    private final TheaterSeatService theaterSeatService;

    @Autowired
    public PaymentApiController(ReservationSeatService reservationSeatService, TheaterSeatService theaterSeatService) {
        this.reservationSeatService = reservationSeatService;
        this.theaterSeatService = theaterSeatService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<String> reserveSeats(@RequestBody List<ReservationSeatDto> reservationSeatDto) {
        try {
            System.out.println("[POST /reservation] 요청 데이터: " + reservationSeatDto);
            reservationSeatService.saveAllReservationsWithDiscount(reservationSeatDto);
            System.out.println("[POST /reservation] 예약 성공");
            return ResponseEntity.ok("예약이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            System.out.println("[POST /reservation] 예약 중 오류 발생:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 중 오류 발생");
        }
    }

    @GetMapping("/reservation/details")
    public ResponseEntity<?> getReservationDetails(
            @RequestParam String movieId,
            @RequestParam String showTime,
            @RequestParam String theaterId,
            @RequestParam String memberId
    ) {
        try {
            System.out.println("[GET /reservation/details] 요청 파라미터:");
            System.out.println("movieId: " + movieId);
            System.out.println("showTime: " + showTime);
            System.out.println("theaterId: " + theaterId);
            System.out.println("memberId: " + memberId);

            List<ReservationSeatDto> reservations = reservationSeatService.findReservations(movieId, showTime, theaterId, memberId);

            if (reservations.isEmpty()) {
                System.out.println("[GET /reservation/details] 예약 데이터가 없음");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 조건에 대한 예약 데이터가 없습니다.");
            }

            System.out.println("[GET /reservation/details] 조회된 데이터: " + reservations);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            System.out.println("[GET /reservation/details] 예약 데이터 조회 중 오류 발생:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 데이터를 가져오는 중 오류 발생");
        }
    }



    @DeleteMapping("/reservation/delete")
    public ResponseEntity<?> deleteReservation(
            @RequestParam String movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showTime,
            @RequestParam String theaterId,
            @RequestParam String memberId
    ) {
        try {
            System.out.println("[DELETE /reservation/delete] 요청 파라미터:");
            System.out.println("movieId: " + movieId);
            System.out.println("showTime: " + showTime);
            System.out.println("theaterId: " + theaterId);
            System.out.println("memberId: " + memberId);

            boolean isDeleted = reservationSeatService.deleteReservation(movieId, showTime, theaterId, memberId);

            if (!isDeleted) {
                System.out.println("[DELETE /reservation/delete] 삭제할 예약 데이터를 찾을 수 없음");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 예약 데이터를 찾을 수 없습니다.");
            }

            System.out.println("[DELETE /reservation/delete] 예약 데이터 삭제 성공");
            return ResponseEntity.ok("예약 데이터가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("[DELETE /reservation/delete] 예약 데이터 삭제 중 오류 발생:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 데이터 삭제 중 오류 발생");
        }
    }


    @PostMapping("/theater/seats/update")
    public ResponseEntity<?> updateSeatStatuses(@RequestBody List<TheaterSeatDto> seatUpdates) {
        try {
            System.out.println("[POST /api/theater/seats/update] 요청 데이터: " + seatUpdates);

            // 서비스에서 좌석 상태 업데이트
            boolean allUpdated = theaterSeatService.updateSeatStatuses(seatUpdates);

            if (!allUpdated) {
                System.out.println("[POST /api/theater/seats/update] 일부 좌석 상태 업데이트 실패");
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("일부 좌석 상태 업데이트 실패");
            }

            System.out.println("[POST /api/theater/seats/update] 모든 좌석 상태 업데이트 성공");
            return ResponseEntity.ok("모든 좌석 상태가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            System.out.println("[POST /api/theater/seats/update] 좌석 상태 업데이트 중 오류 발생:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좌석 상태 업데이트 중 오류 발생");
        }
    }

}
