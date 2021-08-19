package com.example.managinghealthapplicationv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.managinghealthapplicationv1.ui.login.LoginActivity;
import com.example.managinghealthapplicationv1.ui.login.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This code allows the activity to check if a user is signed into firebase or not

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // If User is signed in start home activity
            startActivity(new Intent(SplashScreenActivity.this, WalkingActivity.class));
        }
        else {
            // If user is not signed in start Splashscreen activity, showing MHA logo
            EasySplashScreen configure = new EasySplashScreen(SplashScreenActivity.this)
                    .withFullScreen()
                    .withBackgroundColor(Color.parseColor("#efefef"))
                    .withFooterText("© 2019 - 2020 MHA All Rights Reserved")
                    .withLogo(R.drawable.logo); //MHA Logo


            View easySplashScreen = configure.create();
            setContentView(easySplashScreen);
            // After 2 seconds of the splashscreen displaying open the register activity allowing the user to sign up or sign in
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, RegisterActivity.class));
                }
            }, 2000);   //2 seconds

        }

    }
}
