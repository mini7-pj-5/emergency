let map;
let activePath = null;
let activeMarker = null; // 현재 활성화된 마커
let activeRow = null; // 현재 활성화된 테이블 행
const markers = []; // 모든 마커를 저장할 배열

function initializeMap(hospitals, userLatitude, userLongitude) {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(userLatitude, userLongitude),
        zoom: 15
    });

    // 사용자 위치 표시
    const userMarker = new naver.maps.Marker({
        position: new naver.maps.LatLng(userLatitude, userLongitude),
        map: map,
        icon: {
            content: `
                <div style="
                    width: 20px;
                    height: 20px;
                    background-color: blue;
                    border-radius: 50%;
                    border: 2px solid white;
                    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.5);
                "></div>
            `,
            anchor: new naver.maps.Point(10, 10)
        },
        zIndex: 1000
    });

    // 병원 마커 생성
    hospitals.forEach((hospital, index) => {
        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(hospital.latitude, hospital.longitude),
            map: map,
            icon: createMarkerIcon(index + 1, '#007bff', 'white'), // 기본 마커 스타일
            zIndex: 10
        });

        marker.hospitalIndex = index; // 마커에 병원 인덱스 저장
        markers.push(marker); // 마커를 배열에 저장

        naver.maps.Event.addListener(marker, 'click', function () {
            handleHospitalClick(hospitals, index, marker);
        });
    });

    // 테이블 클릭 이벤트 추가
    addTableClickEvents(hospitals);
}

function addTableClickEvents(hospitals) {
    const table = document.getElementById('hospitalTable');
    if (table) {
        table.addEventListener('click', function (e) {
            const row = e.target.closest('tr');
            if (row && row.rowIndex > 0) {
                const index = row.rowIndex - 1; // 헤더 제외
                const marker = markers[index]; // 해당 인덱스의 마커 가져오기
                handleHospitalClick(hospitals, index, marker);
            }
        });
    } else {
        console.error("테이블을 찾을 수 없습니다.");
    }
}

function handleHospitalClick(hospitals, index, marker = null) {
    const hospital = hospitals[index];
    if (!hospital) return;

    // 이전 마커 초기화
    if (activeMarker) {
        activeMarker.setIcon(createMarkerIcon(activeMarker.hospitalIndex + 1, '#007bff', 'white')); // 기본 마커 복원
    }

    if (marker) {
        marker.setIcon(createMarkerIcon(index + 1, 'red', 'white')); // 강조 마커 스타일
        activeMarker = marker; // 현재 마커 저장
    }

    // 테이블 행 강조
    const table = document.getElementById('hospitalTable');
    if (table) {
        const row = table.rows[index + 1]; // 헤더 제외
        if (activeRow) activeRow.style.backgroundColor = ''; // 이전 행 초기화
        if (row) {
            row.style.backgroundColor = '#f0f8ff'; // 강조 색상
            activeRow = row; // 현재 행 저장
        }
    }

    // 경로 그리기
    drawPath(hospital.latitude, hospital.longitude);

    // 플로팅 패널에 정보 표시
    showFloatingPanel(hospital);

    // 지도 중심 이동
    const position = new naver.maps.LatLng(hospital.latitude, hospital.longitude);
    map.setCenter(position);
}

function createMarkerIcon(number, backgroundColor, textColor) {
    return {
        content: `
            <div style="
                width: 30px;
                height: 30px;
                background-color: ${backgroundColor};
                border: 2px solid white;
                border-radius: 50%;
                text-align: center;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                color: ${textColor};
                font-size: 14px;
                box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
            ">
                ${number}
            </div>
        `,
        anchor: new naver.maps.Point(15, 15) // 중심 맞춤
    };
}

function showFloatingPanel(hospital) {
    const panel = document.querySelector('.floating-info-panel');
    panel.innerHTML = `
        <h4>${hospital.hospitalName}</h4>
        <p><strong>병원 정보:</strong> ${hospital.emergencyMedicalInstitutionType}</p>
        <p><strong>주소:</strong> ${hospital.address}</p>
        <p><strong>전화:</strong> ${hospital.phoneNumber1 || 'N/A'} / ${hospital.phoneNumber3 || 'N/A'}</p>
        <p><strong>위도:</strong> ${hospital.latitude}</p>
        <p><strong>경도:</strong> ${hospital.longitude}</p>
        <p><strong>거리:</strong> ${hospital.distance} km</p>
    `;
    panel.style.display = 'block';
}

function drawPath(hospitalLatitude, hospitalLongitude) {
    if (activePath) {
        activePath.setMap(null);
    }

    const start = `${userLongitude},${userLatitude}`;
    const goal = `${hospitalLongitude},${hospitalLatitude}`;

    fetch(`/api/directions?start=${start}&goal=${goal}`)
        .then(response => response.json())
        .then(data => {
            const path = data.route.traoptimal[0].path.map(coord => new naver.maps.LatLng(coord[1], coord[0]));

            activePath = new naver.maps.Polyline({
                path: path,
                strokeColor: '#5347AA',
                strokeWeight: 5,
                map: map
            });

            map.fitBounds(path);
        })
        .catch(error => {
            console.error("경로 가져오기 실패:", error);
            alert("경로를 가져오는 데 실패했습니다.");
        });
}
