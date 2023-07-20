package com.wisethan.bestrefur1.OnlineOrder;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wisethan.bestrefur1.OnlineOrder.DTO.OnlineOrderDTO;
import com.wisethan.bestrefur1.OnlineOrder.model.OnlineOrderUrl;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.common.RetrofitClient;
import com.wisethan.bestrefur1.databinding.ActivityOnlineOrderBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OnlineOrderActivity extends AppCompatActivity {

    private ActivityOnlineOrderBinding binding;
    private ArrayList<OnlineOrderUrl> imageUrlsList;

    private OnlineOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnlineOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 툴바 설정

        // Up 버튼 활성화
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //액션바가 null이 아닌 경우, Up 버튼을 활성화하고 커스텀 제목("온라인 주문")을 설정
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.online_order_title);
        }

        imageUrlsList = new ArrayList<>();

        RetrofitClient retrofitAPI = new RetrofitClient();

        // API 서비스 생성
        Call<List<OnlineOrderDTO>> jsonArrayCall = retrofitAPI.retrofitAPI.getAllByOnlineOrder();
        jsonArrayCall.enqueue(new Callback<List<OnlineOrderDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<OnlineOrderDTO>> call, @NonNull Response<List<OnlineOrderDTO>> response) {
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (OnlineOrderDTO onlineOrderDTO : response.body()) {
                            Log.e("onResponse", onlineOrderDTO.toString());
                            String productImg = onlineOrderDTO.getOnlineOrderImgUrl();
                            imageUrlsList.add(new OnlineOrderUrl(productImg));
                            runOnUiThread(() -> {
                                RecyclerView recyclerView = findViewById(R.id.online_image_recyclerview);
                                //animtor 설정으로 속도 초기화하려했으나 화면 스크롤 끝부분에 animtor 작동이 안되서 중단
                                //아이템 깜빡임 제거
                                recyclerView.setItemAnimator(null);
                                adapter = new OnlineOrderAdapter(imageUrlsList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(OnlineOrderActivity.this); //GridLayoutManager로 2열설정
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                                //RecyclerView에 하드웨어 가속화가 설정되어있는지 확인
                                Log.e(TAG, "하드웨어 가속화 중인가요 " + recyclerView.isHardwareAccelerated());
                            });
                        }
                    } else {
                        // 데이터가 없을때
                        Toast.makeText(OnlineOrderActivity.this, "이미지가 없습니다", Toast.LENGTH_SHORT).show();
                        Log.e("Response body is null", String.valueOf(response.errorBody()));
                    }
                } else {
                    // 요청 실패
                    Toast.makeText(OnlineOrderActivity.this, "이미지 가져오기 실패", Toast.LENGTH_SHORT).show();
                    Log.e("Response errorBody", String.valueOf(response.errorBody()));
                }
            }


            @Override
            public void onFailure(@NonNull Call<List<OnlineOrderDTO>> call, @NonNull Throwable t) {
                // 네트워크 오류 또는 기타 오류 처리
                t.printStackTrace();
                Toast.makeText(OnlineOrderActivity.this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
            }

        });


        // FloatingActionButton의 클릭 리스너 설정
        FloatingActionButton fabScrollTop = findViewById(R.id.fab_scroll_top);
        fabScrollTop.setOnClickListener(v -> {
            // 맨 위로 스크롤
            binding.onlineImageRecyclerview.smoothScrollToPosition(0);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_online, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_add_online) {
            // UploadOnlineOrderActivity로 이동하는 코드
            Intent intent = new Intent(OnlineOrderActivity.this, UploadOnlineOrderActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
