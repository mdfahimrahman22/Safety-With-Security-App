package com.example.safetywithsecurity.Models;

public class UserSettings {
    private String appTheme,emergencyContactRelation,emergencyContact,nationalSecurityNumber,ambulanceNumber;
    private boolean bloodNeedNotification;

    public UserSettings(String appTheme, String emergencyContactRelation, String emergencyContact, String nationalSecurityNumber, String ambulanceNumber, boolean bloodNeedNotification) {
        this.appTheme = appTheme;
        this.emergencyContactRelation = emergencyContactRelation;
        this.emergencyContact = emergencyContact;
        this.nationalSecurityNumber = nationalSecurityNumber;
        this.ambulanceNumber = ambulanceNumber;
        this.bloodNeedNotification = bloodNeedNotification;
    }

    public UserSettings() {
    }

    public void setAppTheme(String appTheme) {
        this.appTheme = appTheme;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setNationalSecurityNumber(String nationalSecurityNumber) {
        this.nationalSecurityNumber = nationalSecurityNumber;
    }

    public void setAmbulanceNumber(String ambulanceNumber) {
        this.ambulanceNumber = ambulanceNumber;
    }

    public void setBloodNeedNotification(boolean bloodNeedNotification) {
        this.bloodNeedNotification = bloodNeedNotification;
    }

    public String getAppTheme() {
        return appTheme;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getNationalSecurityNumber() {
        return nationalSecurityNumber;
    }

    public String getAmbulanceNumber() {
        return ambulanceNumber;
    }

    public boolean isBloodNeedNotification() {
        return bloodNeedNotification;
    }
}
