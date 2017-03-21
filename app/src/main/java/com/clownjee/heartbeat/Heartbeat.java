package com.clownjee.heartbeat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Clownjee on 2017/3/21.
 */
public class Heartbeat {

    private static final String TAG = Heartbeat.class.getName();

    private static final int DEFAULT_INTERVAL = 1000; //单位毫秒

    private Handler mHeartbeatHandler = null;

    private int mHeartbeatIntervalInMilliseconds = DEFAULT_INTERVAL;

    private OnTickListener mListener = null;

    private Boolean mIsHeartbeatRunning = false;

    private Heartbeat() {
    }

    private Runnable mHeartbeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (mHeartbeatHandler == null) {
                throw new RuntimeException("Haven't set handler to Heartbeat yet");
            }

            if (mListener != null) {
                mListener.onTick(Calendar.getInstance().getTimeInMillis());
            }

            mHeartbeatHandler.postDelayed(mHeartbeatRunnable, mHeartbeatIntervalInMilliseconds);
        }
    };

    public void start() {
        synchronized (mIsHeartbeatRunning) {
            if (mIsHeartbeatRunning) {
                throw new RuntimeException("Heartbeat is started already");
            } else if (mHeartbeatHandler == null) {
                throw new RuntimeException("Haven't set handler to Heartbeat yet");
            } else {
                mIsHeartbeatRunning = true;
                mHeartbeatHandler.post(mHeartbeatRunnable);
                Log.d(TAG, "Heartbeat is started");
            }
        }
    }

    public void stop() {
        synchronized (mIsHeartbeatRunning) {
            if (!mIsHeartbeatRunning) {
                throw new RuntimeException("Heartbeat is stopped already or haven't started yet");
            } else if (mHeartbeatHandler == null) {
                throw new RuntimeException("Haven't set handler to Heartbeat yet");
            } else {
                mIsHeartbeatRunning = false;
                mHeartbeatHandler.removeCallbacks(mHeartbeatRunnable);
                Log.d(TAG, "Heartbeat is stopped");
            }
        }
    }

    public static class Builder {

        private int mHeartbeatIntervalInMilliseconds = DEFAULT_INTERVAL;

        private Looper mLooper = null;

        private OnTickListener mListener;

        public Heartbeat build() {
            Heartbeat Heartbeat = new Heartbeat();
            if (mLooper == null) {
                Heartbeat.mHeartbeatHandler = new Handler();
            } else {
                Heartbeat.mHeartbeatHandler = new Handler(mLooper);
            }
            Heartbeat.mListener = mListener;
            Heartbeat.mHeartbeatIntervalInMilliseconds = mHeartbeatIntervalInMilliseconds;
            return Heartbeat;
        }

        public Builder listener(OnTickListener listener) {
            mListener = listener;
            return this;
        }

        public Builder heartbeatIntervalInMilliseconds(int HeartbeatIntervalInMilliseconds) {
            mHeartbeatIntervalInMilliseconds = HeartbeatIntervalInMilliseconds;
            return this;
        }

        public Builder heartbeatIntervalInSeconds(int heartbeatIntevalInSeconds) {
            mHeartbeatIntervalInMilliseconds = heartbeatIntevalInSeconds * 1000;
            return this;
        }

        public Builder looper(Looper looper) {
            mLooper = looper;
            return this;
        }
    }

    public interface OnTickListener {

        void onTick(long timestampInMilliseconds);
    }
}
