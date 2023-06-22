package com.wisethan.bestrefur1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wisethan.bestrefur1.BoramOrder.BoramActivity;
import com.wisethan.bestrefur1.OnlineOrder.OnlineOrderActivity;
import com.wisethan.bestrefur1.RebornOrder.TheRebornActivity;
import com.wisethan.bestrefur1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Button1ClickListener
        OnlineOrderButtonClickListener onlineOrderButtonClickListener = new OnlineOrderButtonClickListener();
        binding.onlineOrder.setOnClickListener(onlineOrderButtonClickListener);

        // Button2ClickListener
        RebonButtonClickListener rebonButtonClickListener = new RebonButtonClickListener();
        binding.theReborn.setOnClickListener(rebonButtonClickListener);

        // Button3ClickListener
        BoramButtonClickListener boramButtonClickListener = new BoramButtonClickListener();
        binding.boram.setOnClickListener(boramButtonClickListener);

        // ActionBar hide
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }


    // OnlineOrderButtonClickListener 클래스
    class OnlineOrderButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, OnlineOrderActivity.class);
            startActivity(intent);
        }
    }


    // RebonButtonClickListener 클래스
    class RebonButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TheRebornActivity.class);
            startActivity(intent);
        }
    }
    // BoramButtonClickListener 클래스
    class BoramButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, BoramActivity.class);
            startActivity(intent);
        }
    }
}
