package com.wisethan.bestrefur1.BoramOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wisethan.bestrefur1.BoramOrder.model.Boram;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.common.NetworkUtils;
import com.wisethan.bestrefur1.databinding.ActivityBoramBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoramActivity extends AppCompatActivity {
    private ActivityBoramBinding binding;
    private BoramAdapter adapter;
    private ArrayList<Boram> boramsList;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.boram_FSA_title);
        }
        boramsList = new ArrayList<>();
        new Thread(() -> {
            String apiUrl = "https://api.inventory.wisethan.com/catalogue/boram/getAll";
            String jsonData = NetworkUtils.fetchJSONData(apiUrl);
            parseAndSetData(jsonData);
        }).start();

        //최상단 스크롤
        FloatingActionButton fabScrollTop = findViewById(R.id.fab_scroll_top);
        fabScrollTop.setOnClickListener(v -> binding.theBornRecyclerView.smoothScrollToPosition(0));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mContext = getApplicationContext();
        getMenuInflater().inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search_name));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 검색어 입력중 이벤트 제어
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                adapter.notifyDataSetChanged();
                return false;
            }

            // 검색어 완료시 이벤트 제어
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // 뒤로 가기 버튼 동작
            return true; //ture를 반환
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseAndSetData(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productImgUrl = jsonObject.getString("product_img_url");
                String detailImageUrl = jsonObject.getString("detail_img_url");
                String goodsName = jsonObject.getString("goods_name");
                boramsList.add(new Boram(productImgUrl, detailImageUrl, goodsName));
            }
            runOnUiThread(() -> {
                RecyclerView recyclerView = findViewById(R.id.theBorn_recyclerView);
                adapter = new BoramAdapter(boramsList);
                GridLayoutManager layoutManager = new GridLayoutManager(BoramActivity.this, 2); //GridLayoutManager로 2열설정
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
