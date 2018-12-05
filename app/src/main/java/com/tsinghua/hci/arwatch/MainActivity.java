package com.tsinghua.hci.arwatch;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class MainActivity extends WearableActivity {
    TextView uTextLog;
    TextView uTextAccelerometer;
    TextView uTextGyroscope;
    //TextView uTextMegneticField;
    //TextView uTextGravity;

    SensorManager sensorManager;
    InertialSensor inertialSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uTextLog = (TextView) findViewById(R.id.text_log);
        uTextAccelerometer = (TextView) findViewById(R.id.text_accelerometer);
        uTextGyroscope = (TextView) findViewById(R.id.text_gyroscope);
        //uTextMegneticField = (TextView) findViewById(R.id.text_megnetic_field);
        //uTextGravity = (TextView) findViewById(R.id.text_gravity);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        inertialSensor = new InertialSensor(this);

        // Enables Always-on
        setAmbientEnabled();
    }
}
