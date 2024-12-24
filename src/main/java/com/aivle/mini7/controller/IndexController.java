package com.aivle.mini7.controller;

import com.aivle.mini7.client.api.FastApiClient;
import com.aivle.mini7.client.dto.HospitalResponseWrapper;
import com.aivle.mini7.client.dto.Hospital;
import com.aivle.mini7.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aivle.mini7.client.dto.HospitalResponse;


import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final FastApiClient fastApiClient;
    private final LogService logService;

    // 기본 경로 ("/")를 "/index"로 리다이렉트
    @GetMapping("/")
    public String redirectToIndex() {
        log.info("Redirecting to /index");
        return "redirect:/index";
    }

    // "/index" 경로 처리
    @GetMapping("/index")
    public String index() {
        log.info("Rendering index page");
        return "index"; // index.html 렌더링
    }

    @GetMapping("/recommend_hospital")
    public String recommendHospital(@RequestParam String request,
                                    @RequestParam double latitude,
                                    @RequestParam double longitude,
                                    @RequestParam int top_n,
                                    Model model) {
        log.info("Received request for hospital recommendation");
        log.info("Request: {}, Latitude: {}, Longitude: {}, Top_N: {}", request, latitude, longitude, top_n);

        try {
            // FastAPI 호출
            log.info("Calling FastAPI with request: {}", request);
            HospitalResponseWrapper responseWrapper = fastApiClient.getHospital(request, latitude, longitude, top_n);

            // 데이터 추출
            String message = responseWrapper.getMessage();
            int predictedGrade = responseWrapper.getPredictedGrade();
            List<Hospital> hospitals = responseWrapper.getNearestHospitals();
            int searchedRadius = responseWrapper.getSearchedRadius();
            double userLatitude = responseWrapper.getUserLatitude(); // FastAPI 응답의 사용자 위도
            double userLongitude = responseWrapper.getUserLongitude(); // FastAPI 응답의 사용자 경도

            // FastAPI에서 병원 데이터를 제대로 받았는지 확인
            log.info("FastAPI Response - Message: {}", message);
            log.info("FastAPI Response - Predicted Grade: {}", predictedGrade);
            log.info("FastAPI Response - Searched Radius: {}", searchedRadius);
            log.info("FastAPI Response - User Latitude: {}", userLatitude);
            log.info("FastAPI Response - User Longitude: {}", userLongitude);
            log.info("FastAPI Response - Nearest Hospitals: {}", hospitals);

            // 모델에 데이터 추가
            model.addAttribute("message", message);
            model.addAttribute("predictedGrade", predictedGrade);
            model.addAttribute("searchedRadius", searchedRadius);
            model.addAttribute("hospitals", hospitals);
            model.addAttribute("latitude", userLatitude); // FastAPI에서 받은 사용자 위도
            model.addAttribute("longitude", userLongitude); // FastAPI에서 받은 사용자 경도

            // 로그 서비스 호출
            if (hospitals != null && !hospitals.isEmpty()) {
                logService.saveLog(
                        hospitals.stream()
                                .map(hospital -> new HospitalResponse(
                                        hospital.getHospitalName(),
                                        hospital.getAddress(),
                                        hospital.getEmergencyMedicalInstitutionType(),
                                        hospital.getPhoneNumber1(),
                                        hospital.getPhoneNumber3(),
                                        hospital.getLatitude(),
                                        hospital.getLongitude(),
                                        hospital.getDistance()
                                ))
                                .collect(Collectors.toList()),
                        request,
                        userLatitude, // FastAPI에서 받은 사용자 위도
                        userLongitude, // FastAPI에서 받은 사용자 경도
                        predictedGrade
                );
                log.info("LogService: Hospital recommendation log saved.");
            }

            return "recommend_hospital"; // HTML 렌더링
        } catch (Exception e) {
            log.error("Error occurred during hospital recommendation: {}", e.getMessage());
            model.addAttribute("error", "병원 추천 중 오류가 발생했습니다: " + e.getMessage());
            return "error"; // 에러 페이지 렌더링
        }
    }


}
