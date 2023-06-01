package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ImageAdapter;
import com.example.myapplication.databinding.ActivitySecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 툴바 설정
        setSupportActionBar(binding.toolbar);
        // Up 버튼 활성화
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //액션바가 null이 아닌 경우, Up 버튼을 활성화하고 커스텀 제목("온라인 주문")을 설정
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("온라인 주문");
        }

        // 이미지 URL 목록 가져오기
        //"imageUrls" 키로 전달된 이미지 URL 목록을 가져옴.
        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        if (imageUrls != null) {
            // 리사이클러뷰 설정
            RecyclerView recyclerView = findViewById(R.id.image_recyclerview);
            ImageAdapter adapter = new ImageAdapter(imageUrls);
            recyclerView.setAdapter(adapter);
            //리사이클러뷰의 레이아웃 매니저를 LinearLayoutManager로 설정합니다.
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // 아이템 간격 설정
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        } else {
            //imageUrls이 null인 경우 로그
            Log.d("SecondActivity", "No image URLs received!");
        }

        // FloatingActionButton의 클릭 리스너 설정
        FloatingActionButton fabScrollTop = findViewById(R.id.fab_scroll_top);
        fabScrollTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 맨 위로 스크롤
                binding.imageRecyclerview.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 이 ID는 홈 또는 Up 버튼을 나타냅니다.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
