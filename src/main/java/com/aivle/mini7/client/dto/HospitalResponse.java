package com.aivle.mini7.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class HospitalResponse {
    private String hospitalName;
    private String address;
    private String emergencyMedicalInstitutionType;
    private String phoneNumber1;
    private String phoneNumber3;
    private double latitude;
    private double longitude;
    private double distance;
}



