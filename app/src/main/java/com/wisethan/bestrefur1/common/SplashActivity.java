package com.wisethan.bestrefur1.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wisethan.bestrefur1.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);finish();
            }, 3000);//3초 있다가 메인 액티비티로
    }
}
