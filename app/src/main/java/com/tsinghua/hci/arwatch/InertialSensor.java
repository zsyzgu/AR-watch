package com.tsinghua.hci.arwatch;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class InertialSensor implements SensorEventListener {
    MainActivity father;

    Sensor sensorAccelerometer;
    Sensor sensorGyroscope;
    //Sensor sensorGravity;

    //FPS
    long lastSecond;
    int cntAcc;
    int cntGyr;
    int cntGra;

    public InertialSensor(MainActivity father) {
        this.father = father;

        sensorAccelerometer = father.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        father.sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        sensorGyroscope = father.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        father.sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        //sensorGravity = father.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //father.sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_FASTEST);

        lastSecond = System.currentTimeMillis();
        cntAcc = 0;
        cntGyr = 0;
        cntGra = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float timestamp = (System.currentTimeMillis() - father.startTime) / 1000.0f;

        if (event.sensor == sensorAccelerometer) {
            synchronized (sensorAccelerometer) {
                cntAcc++;
                father.uTextAccelerometer.setText(String.format("Acc, %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
                father.updateInfo(String.format("Acc, %.3f, %.2f, %.2f, %.2f\n", timestamp, event.values[0], event.values[1], event.values[2]));
            }
        }
        if (event.sensor == sensorGyroscope) {
            synchronized (sensorGyroscope) {
                cntGyr++;
                father.uTextGyroscope.setText(String.format("Gyr, %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
                father.updateInfo(String.format("Gyr, %.3f, %.2f, %.2f, %.2f\n", timestamp, event.values[0], event.values[1], event.values[2]));
            }
        }
        /* if (event.sensor == sensorGravity) {
            synchronized (sensorGravity) {
                cntGra++;
                father.uTextGravity.setText(String.format("Gra, %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
                father.updateInfo(String.format("Gra, %.3f, %.2f, %.2f, %.2f\n", timestamp, event.values[0], event.values[1], event.values[2]));
            }
        }*/

        if (System.currentTimeMillis() - lastSecond >= 1000) {
            lastSecond = System.currentTimeMillis();
            father.uTextLog.setText(String.format("Acc=%d|Gyr=%d|Gra=%d", cntAcc, cntGyr, cntGra));
            cntAcc = 0;
            cntGyr = 0;
            cntGra = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
