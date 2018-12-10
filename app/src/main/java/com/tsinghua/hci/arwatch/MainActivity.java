package com.tsinghua.hci.arwatch;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends WearableActivity {
    //ui
    TextView uTextLog;
    TextView uTextAccelerometer;
    TextView uTextGyroscope;
    Button uButtonLog;
    Button uButtonNetwork;
    EditText uEditTextIP;

    //sensor
    SensorManager sensorManager;
    InertialSensor inertialSensor;

    //log
    long startTime;
    File fileDirectory;
    PrintWriter logger;
    boolean isLogging;

    //network
    Network network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onCreateUI();
        onCreateSensor();
        onCreateLog();
        onCreateNetwork();

        // Enables Always-on
        setAmbientEnabled();
    }

    void onCreateUI() {
        uTextLog = findViewById(R.id.text_log);
        uTextAccelerometer = findViewById(R.id.text_accelerometer);
        uTextGyroscope = findViewById(R.id.text_gyroscope);
        uButtonLog = findViewById(R.id.button_log);
        uButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLogStatus();
            }
        });
        uButtonNetwork = findViewById(R.id.button_network);
        uButtonNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uButtonNetwork.getText().equals("Net : OFF")) {
                    uButtonNetwork.setText("Net : ON");
                    network.connect(uEditTextIP.getText().toString());
                } else {
                    uButtonNetwork.setText("Net : OFF");
                    network.disconnect();
                }
            }
        });
        uEditTextIP = findViewById(R.id.edit_text_ip);
    }

    void onCreateSensor() {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        inertialSensor = new InertialSensor(this);
    }

    void onCreateLog() {
        startTime = System.currentTimeMillis();
        fileDirectory = this.getApplicationContext().getExternalFilesDir(null);
        isLogging = false;
    }

    void onCreateNetwork() {
        network = new Network(this);
    }

    void changeLogStatus() {
        if (isLogging) {
            changeLogStatus(false);
        } else {
            changeLogStatus(true);
        }
    }

    void changeLogStatus(boolean isLogging) {
        if (this.isLogging != isLogging) {
            this.isLogging = isLogging;
            if (isLogging) {
                // open the log
                try {
                    String fileName = new SimpleDateFormat("yy-MM-dd_HH-mm-ss").format(new Date()) + ".log";
                    logger = new PrintWriter(new FileOutputStream(fileDirectory + "/" + fileName));
                    uButtonLog.setText("LOG : ON");
                } catch (Exception e) {
                    Log.d("file", e.toString());
                }
            } else {
                // close the log
                if (logger != null) {
                    logger.close();
                    uButtonLog.setText("LOG : OFF");
                }
            }
        }
    }
}
