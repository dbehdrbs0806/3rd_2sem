document.addEventListener("DOMContentLoaded", function () {
    const movieForm = document.getElementById("movie-form");
    const scheduleForm = document.getElementById("schedule-form");
    const scheduleBox = document.getElementById("schedule-box");
    const movieSelect = document.getElementById("movie-select");
    const posterInput = document.getElementById("movie-poster");
    const posterPreview = document.getElementById("poster-preview");

    // 포스터 미리보기
    posterInput.addEventListener("change", function () {
        const file = posterInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                posterPreview.src = e.target.result;
                posterPreview.style.display = "block";
            };
            reader.readAsDataURL(file);
        } else {
            posterPreview.style.display = "none";
        }
    });

    // 영화 등록
    movieForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = new FormData(movieForm);

        try {
            const response = await fetch("/admin/api/add_movie", {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                alert("영화가 성공적으로 등록되었습니다.");
                movieForm.reset();
                posterPreview.style.display = "none";
                await updateMovieSelect();
            } else {
                alert("영화 등록에 실패했습니다.");
            }
        } catch (error) {
            console.error("영화 등록 중 오류 발생:", error);
            alert("서버와 연결할 수 없습니다.");
        }
    });

    // 영화 선택 드롭다운 업데이트
    async function updateMovieSelect() {
        try {
            const response = await fetch("/admin/api/movies");
            if (response.ok) {
                const movies = await response.json();
                movieSelect.innerHTML = '<option value="" disabled selected>등록된 영화를 선택하세요</option>';
                movies.forEach(movie => {
                    const option = document.createElement("option");
                    option.value = movie.movieId;
                    option.textContent = `${movie.title} (${movie.genre})`;
                    movieSelect.appendChild(option);
                });
            } else {
                alert("영화 데이터를 가져오는 데 실패했습니다.");
            }
        } catch (error) {
            console.error("영화 데이터 가져오기 중 오류 발생:", error);
            alert("서버와 연결할 수 없습니다.");
        }
    }

    // 상영 시간표 저장
    scheduleForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const movieId = movieSelect.value;
        const date = document.getElementById("screen-date").value;
        const time = document.getElementById("screen-time").value;
        const hall = document.getElementById("screen-hall").value;
        const price = document.getElementById("screen-price").value;

        if (!movieId || !date || !time || !hall || !price) {
            alert("모든 필드를 올바르게 입력해주세요.");
            return;
        }

        const showTime = `${date}T${time}`;
        const scheduleData = {
            movieId: movieId,
            theaterId: hall,
            showTime: showTime,
            price: price
        };

        try {
            const response = await fetch("/admin/api/add_schedule", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(scheduleData),
            });

            if (response.ok) {
                alert("상영 시간표가 성공적으로 저장되었습니다.");
                scheduleForm.reset();
                await fetchSchedule();
            } else {
                const errorText = await response.text();
                alert(`상영 시간표 저장에 실패했습니다: ${errorText}`);
            }
        } catch (error) {
            console.error("상영 시간표 저장 중 오류 발생:", error);
            alert("서버와 연결할 수 없습니다.");
        }
    });

    // 상영 시간표 목록 가져오기
    async function fetchSchedule() {
        try {
            const response = await fetch("/admin/api/schedules");
            if (response.ok) {
                const schedules = await response.json();
                scheduleBox.innerHTML = ""; // 기존 데이터 초기화
                schedules.forEach(schedule => {
                    const scheduleDiv = document.createElement("div");
                    scheduleDiv.classList.add("schedule-item");

                    scheduleDiv.innerHTML = `
                        <p>영화: ${schedule.movieId}</p>
                        <p>상영관: ${schedule.theaterId}</p>
                        <p>시간: ${schedule.showTime}</p>
                        <p>가격: ${schedule.price}</p>
                        <button class="delete-button" data-movie-id="${schedule.movieId}" data-show-time="${schedule.showTime}" data-theater-id="${schedule.theaterId}">
                            삭제
                        </button>
                    `;

                    scheduleBox.appendChild(scheduleDiv);
                });

                // 삭제 버튼에 이벤트 추가
                document.querySelectorAll(".delete-button").forEach(button => {
                    button.addEventListener("click", async function () {
                        const movieId = this.getAttribute("data-movie-id");
                        const showTime = this.getAttribute("data-show-time");
                        const theaterId = this.getAttribute("data-theater-id");

                        try {
                            const response = await fetch(`/admin/api/delete_schedule`, {
                                method: "DELETE",
                                headers: { "Content-Type": "application/json" },
                                body: JSON.stringify({ movieId, showTime, theaterId }),
                            });

                            if (response.ok) {
                                alert("상영 시간표가 성공적으로 삭제되었습니다.");
                                await fetchSchedule();
                            } else {
                                const errorText = await response.text();
                                alert(`삭제 실패: ${errorText}`);
                            }
                        } catch (error) {
                            console.error("상영 시간표 삭제 중 오류 발생:", error);
                            alert("서버와 연결할 수 없습니다.");
                        }
                    });
                });
            } else {
                alert("상영 시간표 데이터를 가져오는 데 실패했습니다.");
            }
        } catch (error) {
            console.error("상영 시간표 데이터 가져오기 중 오류 발생:", error);
            alert("서버와 연결할 수 없습니다.");
        }
    }

    // 초기화
    async function initializePage() {
        await updateMovieSelect();
        await fetchSchedule();
    }

    initializePage();
});
