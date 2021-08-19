package com.example.managinghealthapplicationv1;

// LEGACY STEP COUNTER //

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.managinghealthapplicationv1.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepAlert {

    //all variables created

    private StepDetection simpleStepDetector;
    private SensorManager sensorManager; //accelerometer utilised
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: "; //Text assigned to textview in linking xml
    private int numSteps;
    private TextView TvSteps;
    private Button BtnStop;
    Button btnMedical, btnCalorie;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //inflates custom toolbar produced to allow user to choose options from toolbar menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_legacy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //allows toolbar options to be utilised when a menu option is clicked an intent will be triggered
        switch (item.getItemId()) {
            case R.id.settingslegacy:
                Intent setting = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(setting);
                return true;
            case R.id.aboutlegacy:
                Intent info = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set view to retrieve legacy step counter layout file

        //variables declared in onCreate method

        this.setTitle("Managing Health Application"); //Toolbar title adjusted

        Toolbar toolbar = findViewById(R.id.toolbar); //custom toolbar created to make menu option implementation easier
        setSupportActionBar(toolbar); //toolbar initialised when activity is run

        btnCalorie = findViewById(R.id.start_calorie); //Calorie counter starting class assigned to button

        btnCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Calorie counter class called from intent when 'Calorie Counter' button clicked:
                Intent calories = new Intent(MainActivity.this, CalorieCounter.class);
                startActivity(calories);
            }
        });

        btnMedical = findViewById(R.id.start_medical);

        btnMedical.setOnClickListener(new View.OnClickListener() { //Medical ID class called from intent when 'Medical ID' button clicked:
            @Override
            public void onClick(View v) {
                Intent medical = new Intent(MainActivity.this, LegacyMedicalID.class);
                startActivity(medical);
            }
        });





        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //accelerometer assigned to sensor manager
        simpleStepDetector = new StepDetection(); //class called to incorporate step detection values
        simpleStepDetector.registerListener(this); //step filter successfully assigned to count actual steps and not excessive movement of the phone

        TvSteps = findViewById(R.id.tv_steps);
        BtnStop = findViewById(R.id.btn_stop);
        BtnStop.setTag(1); //tag set so that step counter service can be stopped if tag is changed, to save battery life...


                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST); //accelerometer successfully registered to start taking step count



        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) { //if button is clicked tag is changed accordingly

                int status = (Integer) v.getTag();
                if (status == 1) {
                    BtnStop.setText("Step Counter Service stopped, touch to start"); //button text changed and shown to the user
                    sensorManager.unregisterListener(MainActivity.this); //accelerometer unregistered, step count stopped
                    v.setTag(0); //stop
                } else { //opposite of what happened above happens here, sensor is registered again and tag is changed back to allow step count
                    BtnStop.setText("Step Counter Service active, touch to stop");
                    sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                    v.setTag(1); //start


                }
            }
        });

        addNotification(); //on starting the application the user is notified to keep the application running

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

    @Override
    public void onSensorChanged(SensorEvent event) { //on accelerometer value changed the number of steps go up...
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++; //step added on accelerometer movement which passes step detection filter
        TvSteps.setText(TEXT_NUM_STEPS + numSteps); // step count number shown after string textview created in the variable at the top
    }

}
