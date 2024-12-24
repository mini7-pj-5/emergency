// 병원 데이터와 사용자 위치를 받아 네이버 지도 초기화
function initializeMap(hospitals, userLatitude, userLongitude) {
    let activeInfoWindow = null; // 현재 활성화된 정보창을 추적

    if (!hospitals || hospitals.length === 0) {
        return; // 함수 실행 중단
    }
    try {
        const map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(userLatitude, userLongitude),
            zoom: 12
        });

        // 사용자 위치 표시 (커스텀 HTML을 사용해 항상 최상위에 표시)
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
                anchor: new naver.maps.Point(10, 10) // 아이콘의 중심점 설정
            },
            zIndex: 1000 // 최상위 레이어 설정
        });

        // 병원 데이터 처리
        if (hospitals.length > 0) {
            hospitals.forEach(hospital => {
                const marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(hospital.latitude, hospital.longitude),
                    map: map,
                    zIndex: 10 // 병원 마커의 레이어 우선순위를 낮게 설정
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

                // 마커 클릭 시 정보창 열기
                naver.maps.Event.addListener(marker, 'click', function () {
                    // 기존에 열린 정보창 닫기
                    if (activeInfoWindow) {
                        activeInfoWindow.close();
                    }

                    // 현재 마커의 정보창 열기
                    infoWindow.open(map, marker);
                    activeInfoWindow = infoWindow; // 현재 활성화된 정보창 저장
                });
            });

            // 지도 클릭 시 모든 정보창 닫기
            naver.maps.Event.addListener(map, 'click', function () {
                if (activeInfoWindow) {
                    activeInfoWindow.close();
                    activeInfoWindow = null; // 활성화된 정보창 초기화
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
