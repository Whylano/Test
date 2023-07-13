package com.wisethan.bestrefur1.RebornOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.RebornOrder.model.Reborn;
import com.wisethan.bestrefur1.common.NetworkUtils;
import com.wisethan.bestrefur1.databinding.ActivityTheRebornBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TheRebornActivity extends AppCompatActivity {

    private ActivityTheRebornBinding binding;
    private TheRebornAdapter adapter;
    private ArrayList<Reborn> rebornsList;
    protected Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheRebornBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 액티비티에 툴바를 설정, 홈 버튼을 활성화. 또한, 커스텀 타이틀을 설정
        //setSupportActionBar(binding.toolbar);
        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.the_reborn_title); // Set custom title
        }

        //ArrayList 초기화
        rebornsList = new ArrayList<>();
        new Thread(() -> {
            // API 호출 및 JSON 데이터 가져오기
            String apiUrl = "https://api.inventory.wisethan.com/catalogue/theReborn/getAll";
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
        return super.onCreateOptionsMenu(menu);
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
                rebornsList.add(new Reborn(productImgUrl, detailImageUrl, goodsName));
            }
            runOnUiThread(() -> {
                RecyclerView recyclerView = findViewById(R.id.theBorn_recyclerView);
                adapter = new TheRebornAdapter(rebornsList);
                GridLayoutManager layoutManager = new GridLayoutManager(TheRebornActivity.this, 2); //GridLayoutManager로 2열설정
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
//                RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
//                if(animator instanceof SimpleItemAnimator){
//                    ((SimpleItemAnimator)animator).setSupportsChangeAnimations(fal    se);
//                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
