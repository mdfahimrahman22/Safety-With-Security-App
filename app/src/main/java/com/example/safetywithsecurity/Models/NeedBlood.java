package com.example.safetywithsecurity.Models;

public class NeedBlood {
   private String userName,bloodGrp,phoneNumber,userImg,message,time,date,location,userEmail;

    public NeedBlood(String userName,String userEmail,String bloodGrp, String phoneNumber, String userImg, String message, String time, String date, String location) {
        this.bloodGrp = bloodGrp;
        this.userEmail=userEmail;
        this.phoneNumber = phoneNumber;
        this.userImg = userImg;
        this.message = message;
        this.time = time;
        this.date = date;
        this.location = location;
        this.userName=userName;
    }

    public NeedBlood() {
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserImg() {
        return userImg;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
