document.addEventListener("DOMContentLoaded", async function () {
    const path = window.location.pathname; // "/movies/info/001"
    const movieId = path.split('/').pop(); // "001"

    const moviePoster = document.getElementById("movie-poster");
    const movieTitle = document.getElementById("movie-title");
    const movieDirector = document.getElementById("movie-director");
    const movieGenre = document.getElementById("movie-genre");
    const movieLeadActor = document.getElementById("movie-lead-actor");
    const scheduleList = document.getElementById("schedule-list");
    const bookButton = document.getElementById("book-button");

    // 영화 상세 정보 가져오기
    async function fetchMovieDetails(movieId) {
        const response = await fetch(`/movies/information/${movieId}`);
        if (response.ok) {
            return await response.json();
        }
        throw new Error("영화 정보를 불러오는데 실패했습니다.");
    }

    // 상영 시간표 가져오기
    async function fetchSchedules(movieId) {
        const response = await fetch(`/movies/${movieId}/schedules`);
        if (response.ok) {
            return await response.json();
        }
        throw new Error("상영 시간표를 불러오는데 실패했습니다.");
    }

    // 영화 상세 정보 표시
    try {
        const movie = await fetchMovieDetails(movieId);

        // 절대 경로 변환
        /*
        2. 브라우저의 상대 경로 처리
        브라우저는 현재 URL을 기준으로 상대 경로를 자동으로 처리합니다.
        대 경로로 변환하여 상대 경로 처리를 방지해 해결
        * */
        const absolutePosterPath = `${window.location.origin}/${movie.posterPath}`;
        console.log("Final posterPath:", absolutePosterPath);

        moviePoster.src = absolutePosterPath;

        movieTitle.textContent = movie.title;
        movieDirector.textContent = movie.director;
        movieGenre.textContent = movie.genre;
        movieLeadActor.textContent = movie.leadActor;

    } catch (error) {
        console.error(error);
    }

    // 상영 시간표 표시
    try {
        const schedules = await fetchSchedules(movieId);
        scheduleList.innerHTML = schedules
            .map(
                (schedule, index) => `
            <li>
                <span>${schedule.theaterId} | ${schedule.showTime} | ${schedule.theaterId}관 | ${schedule.price}원</span>
                <button class="book-button" data-index="${index}">좌석 선택</button>
            </li>
        `
            )
            .join("");
        // 버튼 클릭 이벤트 추가
        const bookButtons = document.querySelectorAll(".book-button");
        bookButtons.forEach((button) => {
            button.addEventListener("click", (event) => {
                const index = event.target.dataset.index;
                const selectedSchedule = schedules[index];

                // URL 파라미터를 통해 상영스케줄 데이터 전달
                const scheduleParams = new URLSearchParams({
                    movieId: selectedSchedule.movieId,
                    showTime: selectedSchedule.showTime,
                    theaterId: selectedSchedule.theaterId,
                });

                // 좌석 선택 페이지로 이동
                window.location.href = `/seat-selection?${scheduleParams.toString()}`;
            });
        });
    } catch (error) {
        console.error(error);
    }
});
