package com.example.managinghealthapplicationv1;

// STEP COUNTER FOR NEWER PHONES //

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalkingActivity extends AppCompatActivity implements SensorEventListener {

    //all variables created

    private View decorView; //used to allow going fullscreen in activities

    private static final int REQUEST_STEP_PERMISSION = 1; //allows android to request the user for permission to count steps, needed newer android versions

    SensorManager sensorManager; //stepcountsensor utilised

    TextView tv_steps; //steps are passed into this variable

    boolean running = false; //step count sensor is disabled by default

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //inflates custom toolbar produced to allow user to choose options from toolbar menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //allows toolbar options to be utilised when a menu option is clicked an intent will be triggered
        switch (item.getItemId()) {
            case R.id.caloriecounter:
                Intent calories = new Intent(WalkingActivity.this, CalorieCounter.class);
                startActivity(calories);
                return true;
            case R.id.settings:
                Intent setting = new Intent(WalkingActivity.this, SettingsActivity.class);
                startActivity(setting);
                return true;
            case R.id.about:
                Intent info = new Intent(WalkingActivity.this, AboutActivity.class);
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_steps); //set view to retrieve step counter layout file

        //variables declared in onCreate method

        this.setTitle("Managing Health Application"); //Toolbar title adjusted

        Toolbar toolbar = findViewById(R.id.toolbar); //custom toolbar created to make menu option implementation easier
        setSupportActionBar(toolbar); //toolbar initialised when activity is run

        decorView = getWindow().getDecorView(); //initiates decorview to allow activity to go fullscreen
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars()); //hidesystembars method called where the bottom navigation and status bar can be hidden
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener); //variables for the bottom navigation view have been declared

        if(ContextCompat.checkSelfPermission(this, //when the steps activity is clicked on the bottom navigation view permissions listener is started
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){ //by default the step counter permission is denied hence the user is asked to grant permission
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_STEP_PERMISSION);
        }

        tv_steps = findViewById(R.id.tv_steps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //step counter sensor has now been assigned

        addNotification(); //on starting the application the user is notified to keep the application running

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) { //depending on which fragment is selected the switch will execute code accordingly:
            Fragment selectedFragment = null;

            switch (Item.getItemId()){ //If the steps fragment or medical id fragment is selected the specific fragment will be shown...
                case R.id.nav_walk: selectedFragment = new StepsFragment();
                    break;
                case R.id.nav_medical: selectedFragment = new MedicalIDFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) //Step count sensor required android kitkat or above hence a legacy version starts if lower or a step count is not detected
    @Override
    protected void onResume() { //if the application is started the step count sensor is assigned, on resume it is reassigned
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else{ //this is where the application can either switch to legacy mode or continue with the current activity, if no step counter is detected the legacy class is called and the user is limited to this...
            Toast.makeText(this, "Step sensor not found, starting legacy step counter", Toast.LENGTH_SHORT).show();
            Intent Legacy = new Intent(WalkingActivity.this, MainActivity.class);
            startActivity(Legacy);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = true; //the step counter still runs in the background whilst the app is paused, not closed!
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running){
            tv_steps.setText(String.valueOf(event.values[0])); //when step count sensor detects a step, step added to tv_steps

        }

    }

    private void addNotification()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //notification rules required for android 8 and above...
            NotificationChannel notificationChannel = new NotificationChannel("MHA", "Managing Health Application", NotificationManager.IMPORTANCE_DEFAULT); //id and name of application specified so user knows what application sent the notification

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel); //notification channel created to allow notification to pass through
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MHA") //notification is built here
                .setContentText("Managing Health Application")
                .setSmallIcon(R.mipmap.ic_launcher_round) //MHA stock image placed next to notification
                .setAutoCancel(true)
                .setContentText("Please leave MHA open in the background for an accurate step count reading")
                .setStyle(new NotificationCompat.BigTextStyle()); //allows user to read full text shown, beneficial for smaller phones


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build()); //notification is finally built and when method is called notification is sent to the user on creation of the activity
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onWindowFocusChanged(boolean hasFocus) { //linked to hidesystembars to allow activity to go fullscreen
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private int hideSystemBars() { //makes the activity fullscreen allowing more content to be shown, navigation bar can still be accessed by swiping up from the bottom or top of the screen...
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    }
