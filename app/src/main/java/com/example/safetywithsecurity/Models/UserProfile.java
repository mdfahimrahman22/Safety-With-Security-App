package com.example.safetywithsecurity.Models;

public class UserProfile {
    String profilePic,fullName,email,phone,bloodGruop,password,locationLat,locationLong,userId;

    public UserProfile() {
    }

    public UserProfile(String userId,String profilePic,String fullName, String email, String phone, String bloodGruop, String password, String locationLat, String locationLong) {
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bloodGruop = bloodGruop;
        this.password = password;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.userId = userId;
    }
    public UserProfile(String fullName,String email, String phone,String profilePic,  String bloodGruop) {
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bloodGruop = bloodGruop;
    }
    //SignUp Constructor
    public UserProfile(String userId,String fullName, String email, String phone, String bloodGruop, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bloodGruop = bloodGruop;
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBloodGruop() {
        return bloodGruop;
    }

    public String getPassword() {
        return password;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public String getUserId() {
        return userId;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBloodGruop(String bloodGruop) {
        this.bloodGruop = bloodGruop;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
