package com.tsinghua.hci.arwatch;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class InertialSensor implements SensorEventListener {
    MainActivity father;

    Sensor sensorAccelerometer;
    Sensor sensorGyroscope;

    //FPS
    long lastSecond;
    int cntAcc;
    int cntGyr;

    public InertialSensor(MainActivity father) {
        this.father = father;

        sensorAccelerometer = father.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        father.sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        sensorGyroscope = father.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        father.sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        lastSecond = System.currentTimeMillis();
        cntAcc = 0;
        cntGyr = 0;
    }

    private void updateInfo(String string) {
        if (father.logger != null) {
            father.logger.write(string);
            father.logger.flush();
        }
        if (father.network != null && father.network.isConnected()) {
            father.network.send(string);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float timestamp = (System.currentTimeMillis() - father.startTime) / 1000.0f;
        if (event.sensor == sensorAccelerometer) {
            synchronized (sensorAccelerometer) {
                cntAcc++;
                father.uTextAccelerometer.setText(String.format("Acc, %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
                updateInfo(String.format("Acc, %.3f, %.2f, %.2f, %.2f\n", timestamp, event.values[0], event.values[1], event.values[2]));
            }
        }
        if (event.sensor == sensorGyroscope) {
            synchronized (sensorGyroscope) {
                cntGyr++;
                father.uTextGyroscope.setText(String.format("Gyr, %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
                updateInfo(String.format("Gyr, %.3f, %.2f, %.2f, %.2f\n", timestamp, event.values[0], event.values[1], event.values[2]));
            }
        }
        if (System.currentTimeMillis() - lastSecond >= 1000) {
            lastSecond = System.currentTimeMillis();
            father.uTextLog.setText(String.format("Acc = %d fps; Gyr = %d fps", cntAcc, cntGyr));
            cntAcc = 0;
            cntGyr = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
