// 기본 위치 (서울 좌표)
const defaultLatitude = "36.538435";
const defaultLongitude = "126.989828";

// IP Geolocation API URL
const ipGeolocationAPI = "http://ip-api.com/json/";

// Geolocation API를 사용하여 현재 위치 가져오기
function setLocation() {
    const latitudeInput = document.getElementById("latitude");
    const longitudeInput = document.getElementById("longitude");

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                // Geolocation API에서 위치를 성공적으로 가져온 경우
                latitudeInput.value = position.coords.latitude.toFixed(6);
                longitudeInput.value = position.coords.longitude.toFixed(6);
            },
            function (error) {
                // Geolocation API 실패 시 IP 기반 위치 시도
                console.warn("Geolocation 정보를 가져오지 못했습니다. IP 기반 위치를 시도합니다.");
                fetchIPBasedLocation(latitudeInput, longitudeInput);
            }
        );
    } else {
        // Geolocation API를 지원하지 않는 경우 IP 기반 위치 시도
        console.warn("이 브라우저는 Geolocation을 지원하지 않습니다. IP 기반 위치를 시도합니다.");
        fetchIPBasedLocation(latitudeInput, longitudeInput);
    }
}

// IP 기반 위치 가져오기
function fetchIPBasedLocation(latitudeInput, longitudeInput) {
    fetch(ipGeolocationAPI)
        .then(response => response.json())
        .then(data => {
            if (data && data.lat && data.lon) {
                // IP 기반으로 위치를 성공적으로 가져온 경우
                console.log("IP 기반 위치를 가져왔습니다:", data);
                latitudeInput.value = data.lat.toFixed(6);
                longitudeInput.value = data.lon.toFixed(6);
            } else {
                // IP 기반 위치도 실패 시 기본 위치 설정
                console.warn("IP 기반 위치를 가져오지 못했습니다. 기본 위치를 사용합니다.");
                setDefaultLocation(latitudeInput, longitudeInput);
            }
        })
        .catch(error => {
            console.error("IP 기반 위치를 가져오는 중 오류 발생:", error);
            setDefaultLocation(latitudeInput, longitudeInput); // 기본 위치로 대체
        });
}

// 기본 위치 설정
function setDefaultLocation(latitudeInput, longitudeInput) {
    latitudeInput.value = defaultLatitude;
    longitudeInput.value = defaultLongitude;
    alert("위치 정보를 가져오지 못해 기본 위치를 사용합니다.");
}
