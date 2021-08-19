package com.example.managinghealthapplicationv1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framelayout_actionbar); //set view to blank frame layout to allow settings preferences to be viewed

        if(findViewById(R.id.fragment_actionbarmenu) !=null) //layout file where framelayout is used to show fragments, if not null execute next if statement
        {
            if(savedInstanceState!=null) //this fragment is opened and settings preferences are passed through to this fragment, hence the same 'actionbarmenu' is called
                return;

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_actionbarmenu, new CreateSettingPreferences()).commit(); //the settings page can be seen successfully
        }

    }
}
