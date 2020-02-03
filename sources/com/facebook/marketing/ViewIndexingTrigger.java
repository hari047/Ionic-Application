package com.facebook.marketing;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ViewIndexingTrigger implements SensorEventListener {
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final double SHAKE_THRESHOLD_GRAVITY = 2.700000047683716d;
    private OnShakeListener mListener;
    private int mShakeCount;
    private long mShakeTimestamp;

    public interface OnShakeListener {
        void onShake(int i);
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public void onSensorChanged(SensorEvent event) {
        if (this.mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            double gX = (double) (x / 9.80665f);
            double gY = (double) (y / 9.80665f);
            double gZ = (double) (event.values[2] / 9.80665f);
            if (Math.sqrt((gX * gX) + (gY * gY) + (gZ * gZ)) > SHAKE_THRESHOLD_GRAVITY) {
                long now = System.currentTimeMillis();
                if (this.mShakeTimestamp + 500 <= now) {
                    if (this.mShakeTimestamp + 3000 < now) {
                        this.mShakeCount = 0;
                    }
                    this.mShakeTimestamp = now;
                    this.mShakeCount++;
                    this.mListener.onShake(this.mShakeCount);
                }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void resetCount() {
        this.mShakeCount = 0;
    }
}
