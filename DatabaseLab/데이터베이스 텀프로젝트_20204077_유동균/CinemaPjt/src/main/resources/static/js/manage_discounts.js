document.addEventListener("DOMContentLoaded", async function () {
    const vipInput = document.getElementById("vip-discount");
    const goldInput = document.getElementById("gold-discount");
    const silverInput = document.getElementById("silver-discount");
    const updateButton = document.getElementById("update-discounts");

    // 할인율 초기값 설정
    let currentDiscounts = {};

    // 서버에서 할인율 가져오기
    async function fetchDiscounts() {
        try {
            const response = await fetch("/admin/api/discounts");
            if (response.ok) {
                const discounts = await response.json(); // 'discounts' 변수로 받아야 함
                currentDiscounts = {}; // 객체로 초기화
                discounts.forEach(discount => {
                    currentDiscounts[discount.grade] = discount.discountRate; // 등급별 할인율 저장
                });

                // 가져온 값을 입력 필드에 설정
                const vipDiscount = discounts.find(d => d.grade.toUpperCase() === "VIP");
                const goldDiscount = discounts.find(d => d.grade.toUpperCase() === "GOLD");
                const silverDiscount = discounts.find(d => d.grade.toUpperCase() === "SILVER");

                if (vipDiscount) vipInput.placeholder = `${vipDiscount.discountRate}%`;
                if (goldDiscount) goldInput.placeholder = `${goldDiscount.discountRate}%`;
                if (silverDiscount) silverInput.placeholder = `${silverDiscount.discountRate}%`;
            } else {
                alert("할인율을 가져오는 데 실패했습니다.");
            }
        } catch (error) {
            console.error("오류 발생:", error);
            alert("서버와 연결할 수 없습니다.");
        }
    }


    // 변경된 할인율 서버에 저장
    async function saveDiscounts(newDiscounts) {
        try {
            const response = await fetch("/admin/api/discounts/update", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newDiscounts),
            });
            if (response.ok) {
                const result = await response.text(); // 서버에서 텍스트 응답을 받음
                alert(result); // 텍스트 응답 표시
                fetchDiscounts(); // 할인율 다시 가져오기
            } else {
                alert("할인율 저장에 실패했습니다.");
            }
        } catch (error) {
            console.error("오류 발생:", error);
            alert("서버와 연결할 수 없습니다.");
        }
    }

    // 변경 버튼 클릭 이벤트
    updateButton.addEventListener("click", function () {
        const newDiscounts = {
            VIP: vipInput.value ? parseInt(vipInput.value) : currentDiscounts.VIP,
            Gold: goldInput.value ? parseInt(goldInput.value) : currentDiscounts.Gold,
            Silver: silverInput.value ? parseInt(silverInput.value) : currentDiscounts.Silver,
        };

        // 서버에 변경된 할인율 저장 요청
        saveDiscounts(newDiscounts);
    });

    // 페이지 로드 시 할인율 가져오기
    fetchDiscounts();
});
