package com.example.managinghealthapplicationv1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about); //set view to about xml layout file

        if(findViewById(R.id.fragment_actionbarmenu) !=null) //layout file where framelayout is used to show fragments, if not null execute next if statement
        {
            if(savedInstanceState!=null) //fragment_about (about view) is copied into the framelayout xml to allow the fragment to inflate
                return;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_actionbarmenu, new CreateSettingPreferences()).commit(); //the about page can be seen successfully
        }


    }
}
