document.addEventListener("DOMContentLoaded", async function () {
    const userTableBody = document.getElementById("user-table").querySelector("tbody");

    try {
        // 서버에서 회원 목록 가져오기 /admin/api/users 에서 회원 목록을 가져옴
        const response = await fetch("/admin/api/users");
        if (response.ok) {
            const users = await response.json();
            users.forEach(user => {
                const row = document.createElement("tr");

                // 등급 선택박스로 할 수 있게 html 생성
                const gradeOptions = `
                    <select class="grade-select">
                        <option value="GOLD" ${user.grade === "GOLD" ? "selected" : ""}>GOLD</option>
                        <option value="SILVER" ${user.grade === "SILVER" ? "selected" : ""}>SILVER</option>
                        <option value="VIP" ${user.grade === "VIP" ? "selected" : ""}>VIP</option>
                    </select>
                `;

                // 등급 선택을 처리를 위해 생성 html 생성
                const gradeSaveButton = `
                    <button class="save-grade-btn">저장</button>
                `;

                // 승인 버튼의 처리 함수
                let actionButtons = "";
                if (user.isApproved === 0) { // 승인되지 않은 회원만 버튼 표시
                    actionButtons = `
                        <button class="approve-btn">승인</button>
                    `;
                } else {
                    actionButtons = `<span class="approved-label">승인됨</span>`;
                }
                // 회원의 정보가 뜨게 하는 태그
                row.innerHTML = `
                    <td>${user.memberId}</td>
                    <td>${user.name}</td>
                    <td>${user.phoneNumber}</td>
                    <td>${gradeOptions} ${gradeSaveButton}</td>
                    <td>${actionButtons}</td>
                `;
                userTableBody.appendChild(row);         // 생성한 td(표 형식) 를 추가

                const gradeButton = row.querySelector(".save-grade-btn");   // querySelector로 <>태그의 class의 내용으로 매치 시킴
                const gradeSelect = row.querySelector(".grade-select");                // 위에서 생성한 <select> class로 지정

                // 등급 저장 버튼 클릭 이벤트
                gradeButton.addEventListener("click", async function() {
                    const selectedGrade = gradeSelect.value;

                    try {
                        const gradeUpdateResponse = await fetch("/admin/api/users/update", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                            },
                            body: JSON.stringify({
                                memberId: user.memberId,
                                grade: selectedGrade,
                                isApproved: user.isApproved // 현재 승인 상태 유지
                            }),
                        });

                        if (gradeUpdateResponse.ok) {
                            alert("회원 등급이 성공적으로 업데이트되었습니다.");
                        } else {
                            alert("등급 업데이트 실패!");
                        }
                    } catch (err) {
                        console.error("오류 발생:", err);
                        alert("네트워크 오류가 발생했습니다.");
                    }
                });

                // 승인 버튼을 위한 경로 url과 같은 것을 사용하여 내용을 수정하고 사용함

                // 승인 버튼 클릭 이벤트
                const approveButton = row.querySelector(".approve-btn");
                if (approveButton) {
                    approveButton.addEventListener("click", async function () {
                        const grade = row.querySelector("select").value;

                        try {
                            const updateResponse = await fetch("/admin/api/users/update", {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                },
                                body: JSON.stringify({
                                    memberId: user.memberId,
                                    grade: grade,
                                    isApproved: 1 // 승인 상태로 업데이트
                                }),
                            });

                            if (updateResponse.ok) {
                                alert("회원이 성공적으로 승인되었습니다.");
                                row.querySelector(".approve-btn").remove(); // 승인 버튼 제거
                                row.querySelector("td:last-child").innerHTML = `<span class="approved-label">승인됨</span>`;
                            } else {
                                alert("회원 승인 실패!");
                            }
                        } catch (err) {
                            console.error("오류 발생:", err);
                            alert("네트워크 오류가 발생했습니다.");
                        }
                    });
                }
            });
        } else {
            console.error("회원 목록을 가져오는 데 실패했습니다.");
        }
    } catch (err) {
        console.error("오류 발생:", err);
    }
});
