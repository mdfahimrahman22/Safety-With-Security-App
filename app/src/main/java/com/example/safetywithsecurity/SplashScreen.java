package com.example.safetywithsecurity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.safetywithsecurity.profile.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.time.Year;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN=1500;
    Animation topAnim,bottomAnim;
    Intent intent;
    private FirebaseAuth auth;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView tv=findViewById(R.id.allRightsReserved);
        tv.setText("\u00A9 "+ Year.now()
                .getValue() +" All rights are reserved.");

        ImageView image=findViewById(R.id.imageView);
        TextView welcomeText=findViewById(R.id.welcomeText);
        TextView text=findViewById(R.id.poweredBy);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image.setAnimation(topAnim);
        welcomeText.setAnimation(bottomAnim);
        auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth.getCurrentUser()!=null){
                    intent = new Intent(SplashScreen.this, DashboardMain.class);
                    startActivity(intent);
                }else{
                    intent=new Intent(SplashScreen.this, LoginActivity.class);
                    Pair[] pairs =new Pair[2];
                    pairs[0]=new Pair<View,String>(image,"logo_image");
                    pairs[1]=new Pair<View,String>(welcomeText,"logo_text");
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                    startActivity(intent,options.toBundle());
                }

            }
        },SPLASH_SCREEN);
    }
}