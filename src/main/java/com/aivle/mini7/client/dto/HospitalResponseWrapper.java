package com.aivle.mini7.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HospitalResponseWrapper {

    private String message;

    @JsonProperty("predicted_grade")
    private int predictedGrade;

    @JsonProperty("nearest_hospitals")
    private List<Hospital> nearestHospitals;

    @JsonProperty("searched_radius")
    private int searchedRadius;  // 검색 반경

    @JsonProperty("user_latitude")
    private double userLatitude; // 사용자 위도 추가

    @JsonProperty("user_longitude")
    private double userLongitude; // 사용자 경도 추가

    @Override
    public String toString() {
        return "HospitalResponseWrapper{" +
                "message='" + message + '\'' +
                ", predictedGrade=" + predictedGrade +
                ", nearestHospitals=" + nearestHospitals +
                ", searchedRadius=" + searchedRadius +
                ", userLatitude=" + userLatitude +
                ", userLongitude=" + userLongitude +
                '}';
    }
}
