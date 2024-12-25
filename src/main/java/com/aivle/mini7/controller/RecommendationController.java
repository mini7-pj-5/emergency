package com.aivle.mini7.controller;

import com.aivle.mini7.client.api.FastApiClient;
import com.aivle.mini7.client.dto.HospitalResponseWrapper;
import com.aivle.mini7.client.dto.HospitalResponse;
import com.aivle.mini7.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {

    private final FastApiClient fastApiClient;
    private final LogService logService;

    @GetMapping("/recommend_hospital")
    public String recommendHospital(
            @RequestParam String request,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int top_n,
            Model model) {
        log.info("Received request for hospital recommendation");
        log.info("Request: {}, Latitude: {}, Longitude: {}, Top_N: {}", request, latitude, longitude, top_n);

        try {
            // FastAPI 호출
            HospitalResponseWrapper responseWrapper = fastApiClient.getHospital(request, latitude, longitude, top_n);

            // 데이터 추출
            String message = responseWrapper.getMessage();
            int predictedGrade = responseWrapper.getPredictedGrade();
            var hospitals = responseWrapper.getNearestHospitals();
            int searchedRadius = responseWrapper.getSearchedRadius();
            double userLatitude = responseWrapper.getUserLatitude();
            double userLongitude = responseWrapper.getUserLongitude();

            // FastAPI 응답 로그
            log.info("FastAPI Response: {}", responseWrapper);

            // 모델 데이터 추가
            model.addAttribute("message", message);
            model.addAttribute("predictedGrade", predictedGrade);
            model.addAttribute("searchedRadius", searchedRadius);
            model.addAttribute("hospitals", hospitals);
            model.addAttribute("latitude", userLatitude);
            model.addAttribute("longitude", userLongitude);

            // 로그 저장
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
                        userLatitude,
                        userLongitude,
                        predictedGrade
                );
                log.info("LogService: Hospital recommendation log saved.");
            }

            return "recommend_hospital";
        } catch (Exception e) {
            log.error("Error during hospital recommendation: {}", e.getMessage());
            model.addAttribute("error", "병원 추천 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }
}
