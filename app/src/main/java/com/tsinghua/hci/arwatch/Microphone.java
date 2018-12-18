package com.tsinghua.hci.arwatch;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Microphone {
    final static int SAMPLE_RATE = 8000;
    final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    MainActivity father;

    AudioRecord recorder;
    int minSize; // = 640
    Timer timer;

    public Microphone(MainActivity father) {
        super();
        this.father = father;
    }

    public void start() {
        minSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        recorder = new AudioRecord(AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        recorder.startRecording();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                float amplitude = getAmplitude();
                float timestamp = (System.currentTimeMillis() - father.startTime) / 1000.0f;
                father.uTextMicphone.setText(String.format("Mic, %.2f", amplitude));
                father.updateInfo(String.format("Mic, %.3f, %.2f", timestamp, amplitude));
            }
        }, 1000, 10);
    }

    public void stop() {
        if (recorder != null) {
            recorder.stop();
            recorder = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public float getAmplitude() {
        short[] buffer = new short[minSize];
        recorder.read(buffer, 0, minSize);
        float ampitude = 0;
        for (short s : buffer) {
            if (Math.abs(s) > ampitude) {
                ampitude = Math.abs(s);
            }
        }
        return ampitude;
    }
}
