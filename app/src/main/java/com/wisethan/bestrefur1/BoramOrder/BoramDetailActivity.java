package com.wisethan.bestrefur1.BoramOrder;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.databinding.ActivityBoramDetailItemBinding;

public class BoramDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBoramDetailItemBinding binding =
                ActivityBoramDetailItemBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            String detailTitle = getIntent().getStringExtra("goodsNames");
            actionBar.setTitle(detailTitle);
        }

        PhotoView detail_image_view = findViewById(R.id.detail_image_view);
        FloatingActionButton fab_Scroll_Top = findViewById(R.id.fab_scroll_top);

        String detailImageUrl = getIntent().getStringExtra("detailImageUrl");
        Log.e(TAG, "디테일 URL입니다 : " + detailImageUrl);

        Glide.with(this)
                .load(detailImageUrl)
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))// 원본 이미지의 크기로 설정
                .into(detail_image_view);

        detail_image_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        fab_Scroll_Top.setOnClickListener(v -> {
            ScrollView scrollView = findViewById(R.id.scroll_view); // 수정: 스크롤 가능한 뷰 (ScrollView)를 찾아서 스크롤 탑 기능을 구현합니다.
            if (scrollView != null) {
                scrollView.smoothScrollTo(0, 0); // 맨 위로 스크롤
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
