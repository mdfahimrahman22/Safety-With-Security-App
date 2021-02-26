package com.example.safetywithsecurity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.safetywithsecurity.R.menu.activity_main_drawer;

public class MainDrawer extends AppCompatActivity {
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activity_main_drawer);

    }

    public void emergencyContactFunction(MenuItem item) {
        Intent emergency = new Intent(MainDrawer.this , EmergencyContact.class);
        startActivity(emergency);
    }
}
