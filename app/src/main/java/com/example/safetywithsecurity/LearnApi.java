package com.example.safetywithsecurity;

public class LearnApi {
private String title;
int userId,id;

    public LearnApi(int userId, int id,String title) {
        this.title = title;
        this.userId = userId;
        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

}
