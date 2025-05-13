document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("signup-form");
    const passwordError = document.getElementById("password-error");

    // 현재 로그인한 사용자 정보를 가져와 입력 필드에 채우기
    async function fetchMemberData() {
        try {
            const response = await fetch("/api/members/details"); // 서버에서 로그인된 회원의 정보 가져오기
            if (response.ok) {
                const member = await response.json();
                // 서버로부터 받은 데이터 키에 맞춰 입력 필드 값 설정
                document.getElementById("username").value = member.memberId; // DB와 DTO의 키에 따라 변경
                document.getElementById("name").value = member.name;
                document.getElementById("phone").value = member.phoneNumber;
                document.getElementById("card-number").value = member.cardNumber;
            } else if (response.status === 401) {
                alert("로그인이 필요합니다.");
                window.location.href = "/login"; // 로그인 페이지로 이동
            } else if (response.status === 404) {
                alert("회원 정보를 찾을 수 없습니다.");
            } else {
                alert("회원 정보를 가져오는 데 실패했습니다.");
            }
        } catch (error) {
            console.error("회원 정보 가져오기 오류:", error);
        }
    }

    // 회원 정보 가져오기 실행
    fetchMemberData();

    // 폼 제출 시 입력 검증 및 회원 정보 업데이트
    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        // 입력 값 가져오기
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const confirmPassword = document.getElementById("confirm-password").value.trim();
        const name = document.getElementById("name").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const cardNumber = document.getElementById("card-number").value.trim();

        // 입력 검증
        if (!username || !password || !confirmPassword || !name || !phone || !cardNumber) {
            alert("모든 필드를 입력해주세요.");
            return;
        }
        if (password !== confirmPassword) {
            passwordError.textContent = "비밀번호가 일치하지 않습니다.";
            return;
        }
        passwordError.textContent = "";

        // 서버로 업데이트 요청 보내기
        try {
            const response = await fetch("/api/members/update", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    memberId: username, // 서버에서 사용하는 키에 맞춤
                    password,
                    name,
                    phoneNumber: phone, // 서버에서 사용하는 키에 맞춤
                    cardNumber
                })
            });

            if (response.ok) {
                alert("회원 정보가 성공적으로 수정되었습니다. 다시 로그인해주세요.");
                window.location.href = "/"; // 로그인 페이지로 이동
            } else {
                const errorData = await response.json();
                alert(`회원 정보 수정 실패: ${errorData.message}`);
            }
        } catch (error) {
            console.error("회원 정보 수정 요청 오류:", error);
        }
    });
});
