package com.example.managinghealthapplicationv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.managinghealthapplicationv1.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CreateSettingPreferences extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey); //set view to retrieve settings layout file

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) { //allows settings preferences to work onclick
        String key = preference.getKey(); //each key value for options on settings preferences is different

        switch (key) { //based on which key is selected clicking the correlating option will execute tasks shown below:
            case "ChangeMedical":

                Intent myIntent = new Intent(CreateSettingPreferences.this.getActivity(), AddMedicalIDActivity.class);
                startActivity(myIntent);
                return true;

            case "logout":

                FirebaseAuth.getInstance().signOut(); //signs user out of firebase authentication officially
                Intent logout = new Intent(CreateSettingPreferences.this.getActivity(), LoginActivity.class);
                startActivity(logout);
                return true;


        }
        return super.onOptionsItemSelected((MenuItem) preference);
    }
}
