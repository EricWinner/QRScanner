package com.example.ubuntu.qc_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by EdwardAdmin on 2017/7/15.
 */

public class SplashActivity extends AppCompatActivity{

    private final Handler mHandler = new Handler();
    private static final int TIME_DELAY = 300; //ms

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, WelComeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        },TIME_DELAY);
    }
}
