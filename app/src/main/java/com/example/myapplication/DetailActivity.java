package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.databinding.ActivityTheRebornDetailItemBinding;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    private PhotoView detailImageView;
    private FloatingActionButton fabScrollTop;

    private ActivityTheRebornDetailItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheRebornDetailItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            String detailTitle = getIntent().getStringExtra("goodsNames");
            actionBar.setTitle(detailTitle);
        }

        detailImageView = findViewById(R.id.detailImageView);
        fabScrollTop = findViewById(R.id.fab_scroll_top);

        String detailImageUrl = getIntent().getStringExtra("detailImageUrl");

        Glide.with(this)
                .load(detailImageUrl)
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))// 원본 이미지의 크기로 설정
                .into(detailImageView);
        //System.out.println(detailImageUrl);
        fabScrollTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = findViewById(R.id.scrollView); // 수정: 스크롤 가능한 뷰 (ScrollView)를 찾아서 스크롤 탑 기능을 구현합니다.
                if (scrollView != null) {
                    scrollView.smoothScrollTo(0, 0); // 맨 위로 스크롤
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
