package com.example.safetywithsecurity.Models;

public class RateUs {
    String userName,userRating,userFeedback,date;

    public RateUs(String userName, String userRating, String userFeedback, String date) {
        this.userName = userName;
        this.userRating = userRating;
        this.userFeedback = userFeedback;
        this.date = date;
    }

    public RateUs() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
