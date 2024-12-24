// 음성 인식 초기화
let recognition;
const recordButton = document.getElementById('recordButton');
const requestTextarea = document.getElementById('request');
let isRecording = false;
let endTimeout; // 말 끝난 후 종료를 지연시키기 위한 타이머

if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    recognition = new SpeechRecognition();
    recognition.lang = 'ko-KR'; // 한국어 설정
    recognition.interimResults = true; // 중간 결과 활성화
    recognition.continuous = true; // 계속 듣기 활성화

    recognition.onstart = () => {
        isRecording = true;
        recordButton.textContent = '🎤 녹음 중...';
        recordButton.classList.add('recording');
    };

    recognition.onend = () => {
        isRecording = false;
        recordButton.textContent = '🎤 녹음';
        recordButton.classList.remove('recording');
        clearTimeout(endTimeout);
    };

    recognition.onresult = (event) => {
        const interimTranscript = Array.from(event.results)
            .map(result => result[0].transcript)
            .join('');
        requestTextarea.value = interimTranscript.trim(); // 실시간 업데이트

        // 말이 멈추면 타이머로 종료
        clearTimeout(endTimeout);
        endTimeout = setTimeout(() => {
            recognition.stop();
        }, 2000); // 2초 후 종료
    };

    recordButton.addEventListener('click', () => {
        if (isRecording) {
            recognition.stop();
            clearTimeout(endTimeout); // 녹음 종료 시 타이머 초기화
        } else {
            recognition.start();
        }
    });
} else {
    recordButton.disabled = true;
    recordButton.textContent = '🎤 음성인식 지원 안 됨';
}
