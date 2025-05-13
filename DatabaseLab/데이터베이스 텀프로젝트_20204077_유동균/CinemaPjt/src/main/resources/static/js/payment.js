document.addEventListener("DOMContentLoaded", async () => {
    // HTML 요소 선택
    const movieTitleElement = document.getElementById("movie-id");
    const showTimeElement = document.getElementById("show-time");
    const theaterIdElement = document.getElementById("theater-id");
    const seatInfoElement = document.getElementById("seat-info");
    const memberNameElement = document.getElementById("member-name");
    const memberGradeElement = document.getElementById("member-grade");
    const memberCardElement = document.getElementById("member-card");
    const discountRateElement = document.getElementById("discount-rate");
    const discountedPriceElement = document.getElementById("discounted-price");

    // 할인율 초기값 설정
    let currentDiscounts = {};

    let selectedSeats = [];
    let memberInfo = {};
    let discountRate = 0;
    let discountedPrice = 0;



    // 예매 좌석 데이터 가져오기
    async function fetchReservationData() {
        try {
            const reservationPayload = JSON.parse(sessionStorage.getItem("reservationPayload"));
            if (!reservationPayload || reservationPayload.length === 0) {
                throw new Error("Session에 저장된 예약 데이터가 없습니다.");
            }
            // movieId, showTime, theaterId 추출
            const { movieId, showTime, theaterId, memberId } = reservationPayload[0];

            // showTime 형식 변환 (ISO -> SQL)
            const formattedShowTime = showTime.replace("T", " ");



            // 서버 API 호출
            const response = await fetch(`/api/reservation/details?movieId=${movieId}&showTime=${showTime}&theaterId=${theaterId}&memberId=${memberId}`);
            if (!response.ok) {
                throw new Error("예매 데이터를 가져오는 데 실패했습니다.");
            }

            // 서버 응답 데이터
            const data = await response.json();

            // 좌석 정보를 화면에 표시
            if (data.length > 0) {
                // 동적으로 좌석 정보 표시
                seatInfoElement.textContent = data
                    .map(seat => `${seat.rowNumber}${seat.seatNumber}`) // "A5", "A6" 형식으로 출력
                    .join(", "); // "A5, A6" 형태로 연결
            } else {
                seatInfoElement.textContent = "예약된 좌석이 없습니다.";
            }

            // 추가 정보 업데이트
            movieTitleElement.textContent = data[0].movieId || "미정";
            showTimeElement.textContent = data[0].showTime || "미정";
            theaterIdElement.textContent = data[0].theaterId || "미정";
        } catch (error) {
            console.error("예매 데이터를 가져오는 중 오류 발생:", error);
        }
    }

    // 회원 정보 및 할인율 데이터 가져오기
    async function fetchMemberInfo() {
        try {
            const response = await fetch("/api/members/details"); // 현재 로그인된 회원 정보 가져오기
            if (!response.ok) {
                throw new Error("회원 정보를 가져오는 데 실패했습니다.");
            }
            memberInfo = await response.json();
            memberNameElement.textContent = memberInfo.name || "미정";
            memberGradeElement.textContent = memberInfo.grade || "미정";
            memberCardElement.textContent = memberInfo.cardNumber || "미정";

            await fetchDiscountRate();
        } catch (error) {
            console.error("회원 데이터를 가져오는 중 오류 발생:", error);
        }
    }

    async function fetchDiscountRate() {
        try {
            const response = await fetch("/admin/api/discounts");
            if (!response.ok) {
                throw new Error("할인율 데이터를 가져오는 데 실패했습니다.");
            }

            const discounts = await response.json();
            currentDiscounts = {};
            discounts.forEach(discount => {
                currentDiscounts[discount.grade.toUpperCase()] = discount.discountRate;
            });

            // 회원 등급에 맞는 할인율 매칭
            if (memberInfo.grade) {
                discountRate = currentDiscounts[memberInfo.grade.toUpperCase()] || 0;
                updateDiscountInfo();
            } else {
                console.error("회원 등급 정보가 없습니다.");
            }
        } catch (error) {
            console.error("할인율 데이터를 가져오는 중 오류 발생:", error);
        }
    }

    // 할인 정보 업데이트
    function updateDiscountInfo() {
        memberGradeElement.textContent = memberInfo.grade || "미정";
        discountRateElement.textContent = `${discountRate * 100}%`;
        const originalPrice = selectedSeats.reduce((sum, seat) => sum + seat.price, 0);
        discountedPrice = Math.round(originalPrice * (1 - discountRate));
        discountedPriceElement.textContent = `${discountedPrice} 원`;
    }

    // 4. 결제 처리 함수
    async function processPayment() {
        // 결제가 완료되면 페이지를 영화 선택 화면으로 이동
        alert("결제가 완료되었습니다!");
        window.location.href = "/movies"; // 영화 선택 페이지로 이동
    }


    // 5. 예약 데이터 삭제 함수 (페이지 떠날 때 호출)
    async function deleteReservationData() {
        try {
            const reservationPayload = JSON.parse(sessionStorage.getItem("reservationPayload"));
            if (!reservationPayload || reservationPayload.length === 0) return;

            // movieId, showTime, theaterId 추출
            const { movieId, showTime, theaterId, memberId} = reservationPayload[0];

            // 예약 데이터 삭제 요청
            const response = await fetch(`/api/reservation/delete?movieId=${movieId}&showTime=${showTime}&theaterId=${theaterId}&memberId=${memberId}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                throw new Error("예약 데이터를 삭제하는 데 실패했습니다.");
            }

            console.log("예약 데이터가 성공적으로 삭제되었습니다.");
        } catch (error) {
            console.error("예약 데이터를 삭제하는 중 오류 발생:", error);
        }
    }


    // 뒤로가기 버튼에 이벤트 리스너 추가
    document.querySelector(".back-button").addEventListener("click", async () => {
        await deleteReservationData(); // 예약 데이터 삭제
        window.location.href = "/movies"; // 페이지 이동
    });

    // 결제 버튼 이벤트 리스너 등록
    /*document.getElementById("payment-button").addEventListener("click", async () => {

        const reservationPayload = JSON.parse(sessionStorage.getItem("reservationPayload"));

        try {
            // 서버에 좌석 상태 업데이트 요청
            for (const seat of reservationPayload) {
                const { movieId, showTime, theaterId, rowNumber, seatNumber } = seat;

                const response = await fetch(
                    `/api/theater/seats/update?theaterId=${theaterId}&rowNumber=${rowNumber}&seatNumber=${seatNumber}&showTime=${showTime}&movieId=${movieId}`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({
                            isReserved: true, // 본문에 추가적으로 isReserved를 포함
                        }),
                    }
                );
                if (!response.ok) {
                    throw new Error("좌석 상태 업데이트 실패");
                }
                const result = await response.json();
                console.log("좌석 상태 업데이트 성공:", result);
            }
        } catch (error) {
            console.error("좌석 상태 업데이트 중 오류 발생:", error);
        }
        await processPayment();

    });*/

    document.getElementById("payment-button").addEventListener("click", async () => {
        const reservationPayload = JSON.parse(sessionStorage.getItem("reservationPayload"));

        try {
            // 모든 좌석 데이터를 배열로 준비
            const seatUpdates = reservationPayload.map((seat) => ({
                movieId: seat.movieId,
                showTime: seat.showTime,
                theaterId: seat.theaterId,
                rowNumber: seat.rowNumber,
                seatNumber: seat.seatNumber,
            }));

            // 서버로 데이터 전송
            const response = await fetch("/api/theater/seats/update", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(seatUpdates), // 배열 형태로 전송
            });

            if (!response.ok) {
                throw new Error("좌석 상태 업데이트 실패");
            }

            const result = await response.json();
            console.log("좌석 상태 업데이트 성공:", result);

            // 결제 완료 처리
            await processPayment();
        } catch (error) {
            console.error("좌석 상태 업데이트 중 오류 발생:", error);
        }
    });



    // 초기화
    await fetchReservationData(); // 예매 데이터
    await fetchMemberInfo(); // 회원 정보 및 할인율

});
