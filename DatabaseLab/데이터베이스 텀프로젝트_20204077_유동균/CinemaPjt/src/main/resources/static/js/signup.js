document.getElementById("signup-form").addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document.getElementById("confirm-password").value.trim();
    const name = document.getElementById("name").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const cardNumber = document.getElementById("card-number").value.trim();
    const errorElement = document.getElementById("password-error");

    // 비밀번호 확인
    if (password !== confirmPassword) {
        errorElement.textContent = "비밀번호가 일치하지 않습니다.";
        return;
    } else {
        errorElement.textContent = ""; // 오류 메시지 제거
    }

    // 서버에 보낼 데이터 구성
    const memberData = {
        memberId: username,      // 회원번호 (아이디)
        password: password,      // 비밀번호
        name: name,              // 회원 이름
        phoneNumber: phone,      // 휴대전화
        grade: "SILVER",         // 기본 등급 설정
        cardNumber: cardNumber,  // 카드번호
        isApproved: 0            // 승인 여부 (0: 미승인)
    };

    try {
        // 회원가입 요청 보내기
        const response = await fetch("/api/members/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(memberData), // 데이터를 JSON으로 변환하여 전송
        });

        const result = await response.json();

        if (response.ok) {
            alert("회원가입 요청이 접수되었습니다. 관리자의 승인을 기다려주세요.");
            window.location.href = "/"; // 성공 시 로그인 페이지로 이동
        } else {
            alert(result.message || "회원가입에 실패했습니다.");
        }
    } catch (err) {
        console.error("에러 발생:", err);
        alert("회원가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");
    }
});
