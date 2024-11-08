package com.aivle.mini7.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HospitalResponse {
    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("emergency_medical_institution_type")
    private String emergencyMedicalInstitutionType;

    @JsonProperty("phone_number_1")
    private String phoneNumber1;

    @JsonProperty("phone_number_3")
    private String phoneNumber3;

    @JsonProperty("travel_time")
    private String travelTime;

    @JsonProperty("estimated_arrival_time")
    private String estimatedArrivalTime;

    // Getters and Setters
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyMedicalInstitutionType() {
        return emergencyMedicalInstitutionType;
    }

    public void setEmergencyMedicalInstitutionType(String emergencyMedicalInstitutionType) {
        this.emergencyMedicalInstitutionType = emergencyMedicalInstitutionType;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber3() {
        return phoneNumber3;
    }

    public void setPhoneNumber3(String phoneNumber3) {
        this.phoneNumber3 = phoneNumber3;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(String estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }
}



