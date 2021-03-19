package com.example.safetywithsecurity.Models;

public class AmbulanceDetails {
    String ambulanceServiceName,currentLocation,arrivalTime,phoneNum;

    public AmbulanceDetails(String ambulanceServiceName, String currentLocation, String arrivalTime, String phoneNum) {
        this.ambulanceServiceName = ambulanceServiceName;
        this.currentLocation = currentLocation;
        this.arrivalTime = arrivalTime;
        this.phoneNum = phoneNum;
    }

    public AmbulanceDetails() {
    }

    public String getAmbulanceServiceName() {
        return ambulanceServiceName;
    }

    public void setAmbulanceServiceName(String ambulanceServiceName) {
        this.ambulanceServiceName = ambulanceServiceName;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
