document.addEventListener("DOMContentLoaded", async function () {
    const seatContainer = document.getElementById("seat-container");
    let reservedSeats = []; // 서버에서 받아온 전체 좌석 데이터 저장
    const personCountInput = document.getElementById("person-count");

    let maxSelectable = 0; // 선택 가능한 최대 좌석 수
    const reserveButton = document.getElementById("reserve-button");
    let ticketPrice = 0; // 상영 스케줄에서 가져올 기본 요금

    let theaterType; // 상영관 유형 변수

    // Thymeleaf에서 데이터를 가져오는 부분
    const movieId = /*[[${movieId}]]*/ '';
    const showTime = /*[[${showTime}]]*/ '';
    const theaterId = /*[[${theaterId}]]*/ '';


    async function initialize() {
        try {


            // 요금 정보를 먼저 가져옵니다.
            await fetchTicketPrice();
            // 좌석 데이터를 가져옵니다.
            await fetchReservedSeats();
            // 상영관 유형 표시
            displayTheaterType(theaterType);
        } catch (error) {
            console.error("초기화 중 오류 발생:", error);
        }
    }

    // 서버에서 예약된 좌석 데이터를 가져오기
    async function fetchReservedSeats() {
        try {
            const url = `http://localhost:8080/seats?movieId=${movieId}&showTime=${showTime}&theaterId=${theaterId}`;

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`서버로부터 데이터를 가져오는 데 실패했습니다: ${response.status}`);
            }

            reservedSeats = await response.json(); // JSON 데이터를 파싱
            console.log("받아온 좌석 데이터:", reservedSeats);
            createSeats(theaterId); // 좌석 생성

            theaterType = getTheaterType(theaterId); // 상영관 유형 저장

        } catch (error) {
            console.error("좌석 데이터를 가져오는 중 오류 발생:", error);
        }
    }

    // 상영 스케줄에서 요금 정보 가져오기
    async function fetchTicketPrice() {
        try {
            const url = `http://localhost:8080/schedule?movieId=${movieId}&showTime=${showTime}&theaterId=${theaterId}`;

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`요금 정보를 가져오는 데 실패했습니다: ${response.status}`);
            }

            const schedule = await response.json();
            ticketPrice = schedule.price; // 요금을 저장

            console.log("상영 요금:", ticketPrice);
        } catch (error) {
            console.error("요금 정보를 가져오는 중 오류 발생:", error);
        }
    }

    // 상영관 유형 매핑 함수
    function getTheaterType(theaterId) {
        switch (theaterId) {
            case "1":
                return "2D";
            case "2":
                return "3D";
            case "3":
                return "X-SCREEN";
            default:
                return "알 수 없음";
        }
    }

    // 상영관 유형 표시 함수
    function displayTheaterType(type) {
        const theaterTypeElement = document.getElementById("theater-type");
        if (theaterTypeElement) {
            theaterTypeElement.textContent = `상영관 유형: ${type}`;
        } else {
            console.error("상영관 유형 표시 요소가 없습니다.");
        }
    }

    // 영화관 유형 및 좌석 배열 생성
    function createSeats(theaterId) {
        seatContainer.innerHTML = ""; // 기존 좌석 초기화
        let rows = [];
        let columns = [];
        let colCount = 0;

        // 상영관에 따른 좌석 배열 정의
        switch (theaterId) {
            case "1": // 1관: A, B 열, 10x3 배열
                rows = ["A", "B"];
                columns = ["a", "b", "c"];
                colCount = 10;
                break;
            case "2": // 2관: A, B, C 열, 10x2 배열
                rows = ["A", "B", "C"];
                columns = ["a", "b"];
                colCount = 10;
                break;
            case "3": // 3관: A, B 열, 8x3 배열
                rows = ["A", "B"];
                columns = ["a", "b", "c"];
                colCount = 8;
                break;
            default:
                alert("잘못된 상영관 번호입니다.");
                return;
        }

        // 좌석 UI 생성
        rows.forEach((row) => {
            columns.forEach((column) => {
                const rowDiv = document.createElement("div");
                rowDiv.classList.add("seat-row");
                rowDiv.textContent = `${row}열 ${column}: `;

                for (let i = 1; i <= colCount; i++) {
                    const seatId = `${column}${i}`;
                    const seatButton = document.createElement("button");
                    seatButton.textContent = i; // 좌석 번호
                    seatButton.classList.add("seat");
                    seatButton.dataset.row = row;
                    seatButton.dataset.column = column;
                    seatButton.dataset.number = i;

                    // 예약된 좌석인지 확인
                    const isReserved = reservedSeats.some(
                        (seat) =>
                            seat.rowNumber === row &&
                            seat.seatNumber === seatId &&
                            seat.reserved
                    );

                    if (isReserved) {
                        seatButton.classList.add("reserved");
                        seatButton.disabled = true; // 예약된 좌석은 선택 불가
                    } else {
                        seatButton.addEventListener("click", () =>
                            toggleSeatSelection(seatButton)
                        );
                    }

                    rowDiv.appendChild(seatButton);
                }

                seatContainer.appendChild(rowDiv);
            });
        });
    }

    // 좌석 선택 상태 관리
    let selectedSeats = []; // 선택된 좌석 추적
    function toggleSeatSelection(seatButton) {
        const seat = {
            row: seatButton.dataset.row,
            column: seatButton.dataset.column,
            number: seatButton.dataset.number,
        };

        if (seatButton.classList.contains("selected")) {
            // 선택 해제
            seatButton.classList.remove("selected");
            selectedSeats = selectedSeats.filter(
                (s) =>
                    !(s.row === seat.row &&
                        s.column === seat.column &&
                        s.number === seat.number)
            );
        } else {
            if (selectedSeats.length >= maxSelectable) {
                alert(`최대 ${maxSelectable}개의 좌석만 선택 가능합니다.`);
                return;
            }
            seatButton.classList.add("selected");
            selectedSeats.push(seat);
        }

        console.log("현재 선택된 좌석:", selectedSeats);
    }

    // 인원 수 변경 이벤트
    personCountInput.addEventListener("change", (event) => {
        const count = parseInt(event.target.value, 10);
        if (isNaN(count) || count <= 0) {
            alert("올바른 인원 수를 입력하세요.");
            return;
        }

        maxSelectable = count; // 선택 가능한 최대 좌석 수 설정
        selectedSeats = []; // 선택 초기화
        document.querySelectorAll(".seat.selected").forEach((seat) => {
            seat.classList.remove("selected");
        });
    });

    // 예약 버튼 클릭 이벤트
    reserveButton.addEventListener("click", async () => {
        if (selectedSeats.length === 0) {
            alert("좌석을 선택해주세요.");
            return;
        }
        const totalPrice = ticketPrice * selectedSeats.length;

        const payload = {
            movieId,
            showTime,
            theaterId,
            totalPrice,
            selectedSeats,
        };
        try {
            // POST 요청으로 데이터 전송
            const response = await fetch("/api/reservation", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) {
                throw new Error("서버로 데이터 전송 실패");
            }

            // 결제 페이지로 이동
            window.location.href = "/payment";
        } catch (error) {
            console.error("예약 요청 중 오류 발생:", error);
            alert("예약 요청 중 오류가 발생했습니다.");
        }
    });

    await initialize();
});

