let map;
let activePath = null;

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

    hospitals.forEach((hospital, index) => {
        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(hospital.latitude, hospital.longitude),
            map: map,
            zIndex: 10
        });

        naver.maps.Event.addListener(marker, 'click', function () {
            handleHospitalClick(hospitals, index);
        });
    });
}

function addTableClickEvents() {
    const table = document.getElementById('hospitalTable');
    if (table) {
        table.addEventListener('click', function (e) {
            const row = e.target.closest('tr');
            if (row && row.rowIndex > 0) {
                const index = row.rowIndex - 1; // 헤더 제외
                handleHospitalClick(hospitals, index);
            }
        });
    }
}

function handleHospitalClick(hospitals, index) {
    const hospital = hospitals[index];
    if (!hospital) return;

    // 경로 그리기
    drawPath(hospital.latitude, hospital.longitude);

    // 플로팅 패널에 정보 표시
    showFloatingPanel(hospital);

    // 지도 중심 이동
    const position = new naver.maps.LatLng(hospital.latitude, hospital.longitude);
    map.setCenter(position);
}

function showFloatingPanel(hospital) {
    const panel = document.querySelector('.floating-info-panel');
    panel.innerHTML = `
        <h4>${hospital.hospitalName}</h4>
        <p><strong>병원 정보:</strong> ${hospital.emergencyMedicalInstitutionType}</p>
        <p><strong>주소:</strong> ${hospital.address}</p>
        <p><strong>전화 1:</strong> ${hospital.phoneNumber1 || 'N/A'}</p>
        <p><strong>전화 3:</strong> ${hospital.phoneNumber3 || 'N/A'}</p>
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
