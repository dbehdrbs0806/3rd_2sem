document.addEventListener("DOMContentLoaded", async () => {
    const reservationTableBody = document.querySelector("#reservation-table tbody");

    // 예매 데이터 가져오기
    async function fetchReservationData() {
        try {
            const reservationPayload = JSON.parse(sessionStorage.getItem("reservationPayload"));
            if (!reservationPayload || reservationPayload.length === 0) {
                console.error("Session에 저장된 예약 데이터가 없습니다.");
                reservationTableBody.innerHTML = "<tr><td colspan='6'>예약된 데이터가 없습니다.</td></tr>";
                return;
            }

            // 예약 데이터의 첫 번째 요소를 기반으로 서버에서 데이터 가져오기
            const { movieId, showTime, theaterId, memberId } = reservationPayload[0];
            const response = await fetch(`/api/reservation/details?movieId=${movieId}&showTime=${showTime}&theaterId=${theaterId}&memberId=${memberId}`);

            if (!response.ok) {
                throw new Error("예매 데이터를 가져오는 데 실패했습니다.");
            }

            const data = await response.json();

            // 데이터 렌더링
            if (data.length > 0) {
                renderReservations(data);
            } else {
                reservationTableBody.innerHTML = "<tr><td colspan='6'>예약된 데이터가 없습니다.</td></tr>";
            }
        } catch (error) {
            console.error("예매 데이터를 가져오는 중 오류 발생:", error);
        }
    }

    // 예약 데이터를 테이블에 렌더링
    function renderReservations(reservations) {
        reservationTableBody.innerHTML = ""; // 기존 데이터 초기화

        reservations.forEach((reservation) => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${reservation.movieTitle || "미정"}</td>
                <td>${reservation.showTime || "미정"}</td>
                <td>${reservation.theaterId || "미정"}</td>
                <td>${reservation.rowNumber || ""}${reservation.seatNumber || ""}</td>
                <td>${reservation.amount || "미정"} 원</td>
                <td><button class="delete-btn" data-movie-id="${reservation.movieId}" data-show-time="${reservation.showTime}" data-theater-id="${reservation.theaterId}" data-member-id="${reservation.memberId}">삭제</button></td>
            `;

            reservationTableBody.appendChild(row);
        });

        // 삭제 버튼 이벤트 등록
        document.querySelectorAll(".delete-btn").forEach((button) => {
            button.addEventListener("click", async (event) => {
                const { movieId, showTime, theaterId, memberId } = event.target.dataset;
                await deleteReservationData(movieId, showTime, theaterId, memberId);
                await fetchReservationData(); // 삭제 후 데이터 갱신
            });
        });
    }

    // 예약 데이터 삭제
    async function deleteReservationData(movieId, showTime, theaterId, memberId) {
        try {
            const response = await fetch(`/api/reservation/delete?movieId=${movieId}&showTime=${showTime}&theaterId=${theaterId}&memberId=${memberId}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                throw new Error("예약 데이터를 삭제하는 데 실패했습니다.");
            }

            console.log("예약 데이터가 성공적으로 삭제되었습니다.");
            window.location.reload();
        } catch (error) {
            console.error("예약 데이터를 삭제하는 중 오류 발생:", error);
        }
    }

    // 초기 데이터 로드
    await fetchReservationData();
});
