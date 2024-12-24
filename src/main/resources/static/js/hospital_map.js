let map;
let activeInfoWindow = null;
let activePath = null;

function initializeMap(hospitals, userLatitude, userLongitude) {
    try {
        map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(userLatitude, userLongitude),
            zoom: 12
        });

        // 사용자 위치 표시
        const userLocationOverlay = new naver.maps.Marker({
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
                    ">
                    </div>
                `,
                anchor: new naver.maps.Point(10, 10)
            },
            zIndex: 1000
        });

        // 병원 데이터 처리
        if (hospitals.length > 0) {
            hospitals.forEach(hospital => {
                const marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(hospital.latitude, hospital.longitude),
                    map: map,
                    zIndex: 10
                });

                const infoWindow = new naver.maps.InfoWindow({
                    content: `
                        <div style="padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
                            <h4>${hospital.hospitalName}</h4>
                            <p>${hospital.address}</p>
                            <p>전화: ${hospital.phoneNumber1}</p>
                            <p>거리: ${hospital.distance} km</p>
                        </div>
                    `
                });

                naver.maps.Event.addListener(marker, 'click', function () {
                    if (activeInfoWindow) {
                        activeInfoWindow.close();
                    }
                    infoWindow.open(map, marker);
                    activeInfoWindow = infoWindow;
                });
            });

            naver.maps.Event.addListener(map, 'click', function () {
                if (activeInfoWindow) {
                    activeInfoWindow.close();
                    activeInfoWindow = null;
                }
            });
        } else {
            alert("추천된 병원이 없습니다. 검색 반경을 늘려보세요.");
        }
    } catch (error) {
        console.error("지도 초기화 실패:", error);
        alert("지도를 불러오는 데 실패했습니다. 다시 시도해주세요.");
    }
}

function drawPath(hospitalLatitude, hospitalLongitude) {
    // 기존 경로 제거
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

            // 경로가 모두 보이도록 지도 범위 조정
            map.fitBounds(path);
        })
        .catch(error => {
            console.error("경로 가져오기 실패:", error);
            alert("경로를 가져오는 데 실패했습니다. 다시 시도해주세요.");
        });
}

// 테이블의 행 클릭 이벤트 처리
document.addEventListener('DOMContentLoaded', function() {
    const table = document.querySelector('.hospital-table');
    if (table) {
        table.addEventListener('click', function(e) {
            const row = e.target.closest('tr');
            if (row) {
                const latitude = parseFloat(row.cells[6].textContent);
                const longitude = parseFloat(row.cells[7].textContent);
                drawPath(latitude, longitude);
            }
        });
    }
});
