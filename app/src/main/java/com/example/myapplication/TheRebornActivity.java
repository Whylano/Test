package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.TheRebornAdapter;
import com.example.myapplication.common.NetworkUtils;
import com.example.myapplication.databinding.ActivityTheRebornBinding;
import com.example.myapplication.model.Reborn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


public class TheRebornActivity extends AppCompatActivity {

    private ActivityTheRebornBinding binding;
    private EditText searchEditText;
    private ImageButton searchButton;
    private ImageButton searchToggleButton;
    private RelativeLayout searchLayout;
    private TheRebornAdapter adapter;

    private ArrayList<Reborn> rebornsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheRebornBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 액티비티에 툴바를 설정, 홈 버튼을 활성화. 또한, 커스텀 타이틀을 설정
        setSupportActionBar(binding.toolbar);
        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("THE 리 : 본"); // Set custom title
        }

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchToggleButton = findViewById(R.id.searchToggleButton);
        searchLayout = findViewById(R.id.searchLayout);

        //ArrayList 초기화
        rebornsList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // API 호출 및 JSON 데이터 가져오기
                String apiUrl = "https://api.inventory.wisethan.com/catalogue/theReborn/getAll";
                String jsonData = NetworkUtils.fetchJSONData(apiUrl);
                parseAndSetData(jsonData);
        }}).start();




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString();
                adapter.getFilter().filter(searchText);
            }
        });

        searchToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchLayoutVisibility();
            }
        });


        // 상품 이미지 URL과 상품 이름을 화면에 표시
        if (rebornsList != null) {
            RecyclerView recyclerView = findViewById(R.id.theBorn_recyclerView);
            adapter = new TheRebornAdapter(rebornsList);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2); //GridLayoutManager로 2열설정
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        //최상단 스크롤
        FloatingActionButton fabScrollTop = findViewById(R.id.fab_scroll_top);
        fabScrollTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.theBornRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    // 버튼 클릭 시, 텍스트와 아이콘을 변경합니다.
    private void toggleSearchLayoutVisibility() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            searchLayout.setVisibility(View.GONE);
        } else {
            searchLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // 뒤로 가기 버튼 동작
                return true; //ture를 반환
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void parseAndSetData(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productImgUrl = jsonObject.getString("product_img_url");
                String detailImageUrl = jsonObject.getString("detail_img_url");
                String goodsName = jsonObject.getString("goods_name");
                rebornsList.add(new Reborn(productImgUrl, detailImageUrl, goodsName));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView recyclerView = findViewById(R.id.theBorn_recyclerView);
                    adapter = new TheRebornAdapter(rebornsList);
                    GridLayoutManager layoutManager = new GridLayoutManager(TheRebornActivity.this, 2); //GridLayoutManager로 2열설정
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
