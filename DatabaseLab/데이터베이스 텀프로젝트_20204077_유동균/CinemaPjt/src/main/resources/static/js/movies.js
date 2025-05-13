// "예매하기" 버튼 클릭 시
function bookMovie(movieId) {
    alert(`영화 ID: ${movieId} 예매하기`);
    window.location.href = `/movies/info/${movieId}`;
}

document.addEventListener("DOMContentLoaded", function () {
    const scheduledMoviesContainer = document.getElementById("movies-container");
    const upcomingMoviesContainer = document.getElementById("upcoming-movies");

    // 영화 데이터를 서버에서 가져오기
    async function fetchMovies() {
        try {
            // 현재 상영 중인 영화
            const scheduledResponse = await fetch("/movies/scheduled");
            // 상영 예정 영화
            const upcomingResponse = await fetch("/movies/upcoming");

            if (scheduledResponse.ok && upcomingResponse.ok) {
                const scheduledMovies = await scheduledResponse.json();
                const upcomingMovies = await upcomingResponse.json();

                displayScheduledMovies(scheduledMovies);
                displayUpcomingMovies(upcomingMovies);
            } else {
                console.error("영화 데이터를 가져오는 데 실패했습니다.");
                scheduledMoviesContainer.innerHTML = "<p>현재 상영 중인 영화를 불러올 수 없습니다.</p>";
                upcomingMoviesContainer.innerHTML = "<p>상영 예정 영화를 불러올 수 없습니다.</p>";
            }
        } catch (error) {
            console.error("영화 데이터 가져오기 중 오류:", error);
            scheduledMoviesContainer.innerHTML = "<p>서버와 연결할 수 없습니다.</p>";
            upcomingMoviesContainer.innerHTML = "<p>서버와 연결할 수 없습니다.</p>";
        }
    }

    // 현재 상영 중인 영화 표시
    function displayScheduledMovies(movies) {
        scheduledMoviesContainer.innerHTML = ""; // 기존 목록 초기화

        if (movies.length === 0) {
            scheduledMoviesContainer.innerHTML = "<p>현재 상영 중인 영화가 없습니다.</p>";
            return;
        }

        movies.forEach(movie => {
            const movieCard = createMovieCard(movie, 0);
            scheduledMoviesContainer.appendChild(movieCard);
        });
    }

    // 상영 예정 영화 표시
    function displayUpcomingMovies(moviesByGenre) {
        upcomingMoviesContainer.innerHTML = ""; // 기존 목록 초기화

        if (Object.keys(moviesByGenre).length === 0) {
            upcomingMoviesContainer.innerHTML = "<p>상영 예정 영화가 없습니다.</p>";
            return;
        }

        for (const [genre, movies] of Object.entries(moviesByGenre)) {
            // 장르별 섹션 생성
            const genreSection = document.createElement("div");
            genreSection.classList.add("genre-section");
            genreSection.innerHTML = `<h2>${genre}</h2>`;
            const movieList = document.createElement("div");
            movieList.classList.add("movie-list");

            movies.forEach(movie => {
                const movieCard = createMovieCard(movie, 1);
                movieList.appendChild(movieCard);
            });

            genreSection.appendChild(movieList);
            upcomingMoviesContainer.appendChild(genreSection);
        }
    }


    // 영화 카드 생성
    function createMovieCard(movie, upcoming) {
        const movieCard = document.createElement("div");
        movieCard.classList.add("movie-card");
        if (parseInt(upcoming) === 0) {
            movieCard.innerHTML = `
            <img src="${movie.posterPath}" alt="${movie.title}">
            <h3>${movie.title}</h3>
            <button onclick="bookMovie('${movie.movieId}')">예매하기</button>
        `;
        }
        else {
            movieCard.innerHTML = `
            <img src="${movie.posterPath}" alt="${movie.title}">
            <h3>${movie.title}</h3>
            `;
        }
        return movieCard;
    }



    // 영화 데이터 가져오기 실행
    fetchMovies();
});
