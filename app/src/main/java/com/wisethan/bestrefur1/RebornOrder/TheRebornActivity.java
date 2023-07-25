package com.wisethan.bestrefur1.RebornOrder;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.RebornOrder.DTO.RebornDTO;
import com.wisethan.bestrefur1.RebornOrder.model.Reborn;
import com.wisethan.bestrefur1.common.RetrofitClient;
import com.wisethan.bestrefur1.databinding.ActivityTheRebornBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.the_reborn_title); // Set custom title
        }
        //ArrayList 초기화
        rebornsList = new ArrayList<>();

        RetrofitClient retrofitAPI = new RetrofitClient();

        Call<List<RebornDTO>> jsonArrayCall = retrofitAPI.retrofitAPI.getAllByReborn();
        jsonArrayCall.enqueue(new Callback<List<RebornDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<RebornDTO>> call, @NonNull Response<List<RebornDTO>> response) {
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (RebornDTO RebornDTO : response.body()) {
                            Log.e("onResponse", RebornDTO.toString());
                            String goodsName = RebornDTO.getRebornName();
                            String productImg = RebornDTO.getRebornImgUrl();
                            String detailImg = RebornDTO.getRebornDetailUrl();
                            rebornsList.add(new Reborn(productImg, detailImg, goodsName));
                            runOnUiThread(() -> {
                                RecyclerView recyclerView = findViewById(R.id.theBorn_recyclerView);
                                //animtor 설정으로 속도 초기화하려했으나 화면 스크롤 끝부분에 animtor 작동이 안되서 중단
                                //아이템 깜빡임 제거
                                recyclerView.setItemAnimator(null);
                                adapter = new TheRebornAdapter(rebornsList);
                                GridLayoutManager layoutManager = new GridLayoutManager(TheRebornActivity.this, 2); //GridLayoutManager로 2열설정
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                                //RecyclerView에 하드웨어 가속화가 설정되어있는지 확인
                                Log.e(TAG, "하드웨어 가속화 중인가요 " + recyclerView.isHardwareAccelerated());
                            });
                        }
                    } else {
                        // 데이터가 없을때
                        Toast.makeText(TheRebornActivity.this, "이미지가 없습니다", Toast.LENGTH_SHORT).show();
                        Log.e("Response body is null", String.valueOf(response.errorBody()));
                    }
                } else {
                    // 요청 실패
                    Toast.makeText(TheRebornActivity.this, "이미지 가져오기 실패", Toast.LENGTH_SHORT).show();
                    Log.e("Response errorBody", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RebornDTO>> call, @NonNull Throwable t) {
                // 네트워크 오류 또는 기타 오류 처리
                t.printStackTrace();
                Toast.makeText(TheRebornActivity.this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
            }

        });
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
}
