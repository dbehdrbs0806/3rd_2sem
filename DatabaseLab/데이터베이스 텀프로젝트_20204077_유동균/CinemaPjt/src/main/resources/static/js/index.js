document.getElementById("login-form").addEventListener("submit", async function (e) {
    e.preventDefault(); // 폼의 기본 제출 동작 방지

    const username = document.getElementById("username").value.trim(); // 아이디 입력값 가져오기
    const password = document.getElementById("password").value.trim(); // 비밀번호 입력값 가져오기
    const formElement = document.getElementById("login-form"); // 폼 요소 참조

    // 기존 오류 메시지 제거
    const existingError = document.querySelector("#login-form .error-message");
    if (existingError) {
        existingError.remove();
    }

    try {
        // 관리자 계정 확인
        if (username === "admin" && password === "1111") {
            alert("관리자 로그인 성공!");
            window.location.href = "/admin"; // 관리자 페이지로 이동
            return;
        }

        // 일반 사용자 로그인 요청
        const response = await fetch("/api/members/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ memberId: username, password: password }), // JSON 형식으로 데이터 전송
        });

        const result = await response.json(); // 서버 응답 파싱

        if (response.ok) {
            alert("로그인 성공!");               // 성공 메시지 표시
            window.location.href = "/movies";  // 성공 시 영화 선택 페이지로 이동 /movies로

        } else {
            // 서버에서 반환된 에러 메시지 표시
            const errorMessage = document.createElement("p");
            errorMessage.textContent = result.message || "로그인에 실패했습니다.";
            errorMessage.style.color = "red";
            errorMessage.className = "error-message"; // 중복 방지를 위한 클래스 추가
            formElement.appendChild(errorMessage);
        }
    } catch (err) {
        console.error("에러 발생:", err);

        // 네트워크 오류 처리
        const errorMessage = document.createElement("p");
        errorMessage.textContent = "로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.";
        errorMessage.style.color = "red";
        errorMessage.className = "error-message"; // 중복 방지를 위한 클래스 추가
        formElement.appendChild(errorMessage);
    }
});
