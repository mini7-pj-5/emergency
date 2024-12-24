/**
 * 입력 값 검증 함수
 * @param {HTMLElement} inputElement - 입력 요소
 * @param {HTMLElement} errorElement - 에러 메시지 요소
 * @param {HTMLElement} buttonElement - Submit 버튼 요소
 * @param {function} validationFunction - 검증 로직 함수
 */
function validateInput(inputElement, errorElement, buttonElement, validationFunction) {
    const errorMessage = errorElement.getAttribute("data-message"); // HTML에서 에러 메시지 텍스트 가져오기

    if (!validationFunction(inputElement)) {
        errorElement.textContent = errorMessage; // 에러 메시지 설정
        errorElement.style.display = "block"; // 에러 메시지 표시
        disableSubmitButton(buttonElement);
    } else {
        errorElement.style.display = "none"; // 에러 메시지 숨김
        const allErrors = document.querySelectorAll(".error-message");
        const hasError = Array.from(allErrors).some((error) => error.style.display === "block");
        if (!hasError) {
            enableSubmitButton(buttonElement);
        }
    }
}

/**
 * 필수 값 검증 함수
 * @param {HTMLElement} inputElement - 입력 요소
 * @returns {boolean} - 값이 비어있지 않은지 여부
 */
function isNotEmpty(inputElement) {
    return inputElement.value.trim() !== "";
}

/**
 * 범위 값 검증 함수
 * @param {HTMLElement} inputElement - 입력 요소
 * @param {number} maxValue - 허용 최대 값
 * @returns {boolean} - 값이 최대 값 이하인지 여부
 */
function isInRange(inputElement, maxValue) {
    return inputElement.value > 0 && inputElement.value <= maxValue;
}

/**
 * Submit 버튼 비활성화
 * @param {HTMLElement} buttonElement - Submit 버튼 요소
 */
function disableSubmitButton(buttonElement) {
    buttonElement.disabled = true;
    buttonElement.classList.remove("submit-enabled");
    buttonElement.classList.add("submit-disabled");
}

/**
 * Submit 버튼 활성화
 * @param {HTMLElement} buttonElement - Submit 버튼 요소
 */
function enableSubmitButton(buttonElement) {
    buttonElement.disabled = false;
    buttonElement.classList.remove("submit-disabled");
    buttonElement.classList.add("submit-enabled");
}

/**
 * 공통 이벤트 핸들러 설정 함수
 * @param {Array} fields - 각 입력 필드와 에러 메시지, 검증 함수의 배열
 * @param {HTMLElement} buttonElement - Submit 버튼 요소
 */
function addValidationListeners(fields, buttonElement) {
    fields.forEach(({ input, error, validator }) => {
        input.addEventListener("input", () => {
            validateInput(input, error, buttonElement, validator);
        });
    });
}

// 이벤트 리스너 추가
document.addEventListener("DOMContentLoaded", () => {
    const fields = [
        {
            input: document.getElementById("top_n"),
            error: document.getElementById("top_n-error"),
            validator: (input) => isInRange(input, 3)
        },
        {
            input: document.querySelector("textarea[name='request']"),
            error: document.getElementById("request-error"),
            validator: isNotEmpty
        },
        {
            input: document.getElementById("latitude"),
            error: document.getElementById("latitude-error"),
            validator: isNotEmpty
        },
        {
            input: document.getElementById("longitude"),
            error: document.getElementById("longitude-error"),
            validator: isNotEmpty
        }
    ];

    const submitButton = document.getElementById("submit-button");

    // 필드별 이벤트 핸들러 추가
    addValidationListeners(fields, submitButton);

    // 폼 제출 시 모든 입력 값 확인
    document.querySelector("form").addEventListener("submit", (event) => {
        let isValid = true;

        fields.forEach(({ input, error, validator }) => {
            if (!validator(input)) {
                error.textContent = error.getAttribute("data-message");
                error.style.display = "block";
                isValid = false;
            }
        });

        if (!isValid) {
            event.preventDefault(); // 폼 제출 중단
        }
    });
});
