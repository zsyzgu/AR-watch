package com.tsinghua.hci.arwatch;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class InertialSensor implements SensorEventListener {
    MainActivity father;

    Sensor sensorAccelerometer;
    Sensor sensorGyroscope;
    /*Sensor sensorMegneticField;
    Sensor sensorGravity;*/

    public InertialSensor(MainActivity father) {
        this.father = father;

        sensorAccelerometer = father.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        father.sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        sensorGyroscope = father.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        father.sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        /*sensorMegneticField = father.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        father.sensorManager.registerListener(this, sensorMegneticField, SensorManager.SENSOR_DELAY_FASTEST);

        sensorGravity = father.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        father.sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_FASTEST);*/
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == sensorAccelerometer) {
            father.uTextAccelerometer.setText(String.format("Acc: %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
        }
        if (event.sensor == sensorGyroscope) {
            father.uTextGyroscope.setText(String.format("Gyr: %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
        }
        /*if (event.sensor == sensorMegneticField) {
            father.uTextMegneticField.setText(String.format("Meg: %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
        }
        if (event.sensor == sensorGravity) {
            father.uTextGravity.setText(String.format("Gra: %.2f, %.2f, %.2f", event.values[0], event.values[1], event.values[2]));
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
