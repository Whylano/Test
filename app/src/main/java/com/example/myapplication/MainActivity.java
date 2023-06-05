package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.common.NetworkUtils;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.Reborn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Button1ClickListener
        Button1ClickListener button1ClickListener = new Button1ClickListener();
        binding.button1.setOnClickListener(button1ClickListener);

        // Button2ClickListener
        Button2ClickListener button2ClickListener = new Button2ClickListener();
        binding.button2.setOnClickListener(button2ClickListener);
    }

    // Button1ClickListener 클래스
    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // API 호출 및 JSON 데이터 가져오기
                    String apiUrl = "https://api.inventory.wisethan.com/catalogue/daemyung-online/getAll";
                    String jsonData = NetworkUtils.fetchJSONData(apiUrl);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // JSON 데이터 파싱하여 이미지 URL들을 모으기
                                JSONArray jsonArray = new JSONArray(jsonData);
                                ArrayList<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String imageUrl = jsonObject.getString("product_img_url");
                                    imageUrls.add(imageUrl);
                                }
                                displayImages(imageUrls);  // 모아진 이미지 URL들을 한번에 전달
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        }
    }


    // Button2ClickListener 클래스
    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TheRebornActivity.class);
            startActivity(intent);
        }
    }

    // 이미지들을 SecondActivity로 전달하여 표시
    private void displayImages(ArrayList<String> imageUrls) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putStringArrayListExtra("imageUrls", imageUrls);
        startActivity(intent);
    }
}
