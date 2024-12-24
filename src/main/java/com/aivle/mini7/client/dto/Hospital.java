package com.aivle.mini7.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hospital {

    @JsonProperty("hospitalName")
    private String hospitalName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("emergencyMedicalInstitutionType")
    private String emergencyMedicalInstitutionType;

    @JsonProperty("phoneNumber1")
    private String phoneNumber1;

    @JsonProperty("phoneNumber3")
    private String phoneNumber3;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("distance")
    private double distance;
}
