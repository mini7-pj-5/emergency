let map, marker;

/**
 * 네이버 지도 초기화
 */
function initializeMap() {
    const latitudeInput = document.getElementById("latitude");
    const longitudeInput = document.getElementById("longitude");

    const defaultLatitude = parseFloat(latitudeInput.value) || 37.5665;
    const defaultLongitude = parseFloat(longitudeInput.value) || 126.9780;

    // 지도 초기화
    map = new naver.maps.Map("map", {
        center: new naver.maps.LatLng(defaultLatitude, defaultLongitude),
        zoom: 12
    });

    // 마커 초기화
    marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(defaultLatitude, defaultLongitude),
        map: map,
        draggable: true
    });

    // 마커 드래그 이벤트
    naver.maps.Event.addListener(marker, "dragend", function (event) {
        const position = event.coord;
        updatePosition(position.lat(), position.lng(), latitudeInput, longitudeInput);
    });

    // 지도 클릭 이벤트
    naver.maps.Event.addListener(map, "click", function (event) {
        const position = event.coord;
        marker.setPosition(position); // 마커 위치 업데이트
        updatePosition(position.lat(), position.lng(), latitudeInput, longitudeInput);
    });

    // 입력 필드 변경 시 마커 및 지도 위치 업데이트
    latitudeInput.addEventListener("input", updateMarkerPosition);
    longitudeInput.addEventListener("input", updateMarkerPosition);
}

/**
 * 입력 값 변경 시 마커 및 지도 위치 업데이트
 */
function updateMarkerPosition() {
    const latitudeInput = document.getElementById("latitude");
    const longitudeInput = document.getElementById("longitude");

    const newLatitude = parseFloat(latitudeInput.value);
    const newLongitude = parseFloat(longitudeInput.value);

    if (!isNaN(newLatitude) && !isNaN(newLongitude)) {
        const newPosition = new naver.maps.LatLng(newLatitude, newLongitude);
        marker.setPosition(newPosition); // 마커 이동
        map.setCenter(newPosition); // 지도 중심 이동
    }
}

/**
 * 위치 설정 후 지도 초기화
 */
function setLocationAndInitializeMap() {
    setLocation(); // Geolocation 또는 기본 위치 설정
    setTimeout(initializeMap, 1000); // 위치 설정 후 지도 초기화
}

/**
 * 입력 필드와 지도/마커 동기화
 * @param {number} lat - 위도
 * @param {number} lng - 경도
 * @param {HTMLElement} latitudeInput - 위도 입력 필드
 * @param {HTMLElement} longitudeInput - 경도 입력 필드
 */
function updatePosition(lat, lng, latitudeInput, longitudeInput) {
    latitudeInput.value = lat.toFixed(6); // 위도 업데이트
    longitudeInput.value = lng.toFixed(6); // 경도 업데이트
}
