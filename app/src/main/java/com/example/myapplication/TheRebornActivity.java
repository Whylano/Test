package com.example.myapplication;

import static android.content.ContentValues.TAG;

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
    private ArrayList<Reborn> reborns = new ArrayList<>();

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                // API 호출 및 JSON 데이터 가져오기
                String apiUrl = "https://api.inventory.wisethan.com/catalogue/theReborn/getAll";
                final String jsonData = fetchJSONData(apiUrl);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // JSON 데이터 파싱하여 이미지 URL과 goods_name들을 모으기
                            JSONArray jsonArray = new JSONArray(jsonData);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ;
                                String rebronImgUrl = jsonObject.getString("product_img_url"); // product_img_url 추출
                                String goodsName = jsonObject.getString("goods_name"); // detail_img_url 추출
                                String detailUrl = jsonObject.getString("detail_img_url");          // goods_name 추출
//                                    reborn.setProductImgUrl(rebronImgUrl);
//                                    reborn.setGoodsName(goodsName);
//                                    reborn.setDetailUrl(detailUrl);

                                reborns.add(new Reborn(rebronImgUrl,goodsName,detailUrl));
                                //Log.e(TAG,i+"reborns번째 인덱스 입니다"+"\n"+reborns.get(i).getDetailUrl());
                            }
                            //Log.e(TAG,reborns.get(0).getDetailUrl());
                            //Log.e(TAG,"제발나와라"+reborns.get(0)); // 모아진 이미지 URL과 goods_name들, detail_img_url들을 한번에 전달
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,"reborn Thread 에러!!!!");
                        }
                    }
                });
            }
        }).start();


        // 상품 이미지 URL과 상품 이름을 화면에 표시
        if (reborns!=null) {
            RecyclerView recyclerView = findViewById(R.id.theBorn_recyclerView);
            adapter = new TheRebornAdapter(reborns);
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
    private String fetchJSONData(String apiUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //매개변수로 주어진 API URL을 사용하여 연결을 설정
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            //GET 요청 메서드를 설정
            connection.setRequestMethod("GET");
            //데이터 읽어옴
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //예외
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();

                    //예외
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //stringBuilder 문자열반환
        return stringBuilder.toString();
    }
}
