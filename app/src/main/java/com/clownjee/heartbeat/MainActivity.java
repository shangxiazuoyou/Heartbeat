package com.clownjee.heartbeat;

import android.databinding.DataBindingUtil;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clownjee.heartbeat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Heartbeat.OnTickListener {

    private Heartbeat mHeartbeat;

    private ActivityMainBinding mBinding;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mHeartbeat = new Heartbeat.Builder().listener(this).looper(Looper.getMainLooper()).heartbeatIntervalInSeconds(5).build();
        mHeartbeat.start();
    }

    @Override
    public void onTick(long timestampInMilliseconds) {
        mBinding.tvShow.setText(index + "");
        ++index;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHeartbeat) {
            mHeartbeat.stop();
        }
    }
}
