package com.wisethan.bestrefur1.OnlineOrder;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wisethan.bestrefur1.OnlineOrder.model.OnlineOrderUrl;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.common.NetworkUtils;
import com.wisethan.bestrefur1.databinding.ActivityOnlineOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OnlineOrderActivity extends AppCompatActivity {

    private ActivityOnlineOrderBinding binding;
    private ArrayList<OnlineOrderUrl> imageUrlsList;

    private OnlineOrderAdapter adapter;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnlineOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 툴바 설정
        //setSupportActionBar(binding.toolbar);
        // Up 버튼 활성화
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //액션바가 null이 아닌 경우, Up 버튼을 활성화하고 커스텀 제목("온라인 주문")을 설정
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.online_order_title);
        }

        imageUrlsList = new ArrayList<>();
        new Thread(() -> {
            // API 호출 및 JSON 데이터 가져오기
            String apiUrl = "https://api.inventory.wisethan.com/catalogue/daemyung-online/getAll";
            String jsonData = NetworkUtils.fetchJSONData(apiUrl);
            parseAndSetData(jsonData);
        }).start();
        // FloatingActionButton의 클릭 리스너 설정
        FloatingActionButton fabScrollTop = findViewById(R.id.fab_scroll_top);
        fabScrollTop.setOnClickListener(v -> {
            // 맨 위로 스크롤
            binding.imageRecyclerview.smoothScrollToPosition(0);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_online, menu);
        return true;
    }

    //    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            // TODO : 가로 모드 일때
//            PhotoView photoView = findViewById(R.id.photo_view);
//            photoView.setScaleType(ImageView.ScaleType.CENTER);
//            Log.e(TAG,"가로모드 입니다");
//        } else
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // TODO : 세로 모드 일때
//
//        }
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 이 ID는 홈 또는 Up 버튼을 나타냅니다.
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            clickListener();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clickListener() {
    }


    private void parseAndSetData(String jsonData) {
        try {
            // JSON 데이터 파싱하여 이미지 URL들을 모으기
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imageUrl = jsonObject.getString("product_img_url");
                imageUrlsList.add(new OnlineOrderUrl(imageUrl));
            }
            runOnUiThread(() -> {
                Log.e(TAG, imageUrlsList.get(0) + "json객체 데이터입니다");
                //ArrayList 초기화
                // 이미지 URL 목록 가져오기
                //"imageUrls" 키로 전달된 이미지 URL 목록을 가져옴.
                // 리사이클러뷰 설정
                RecyclerView recyclerView = findViewById(R.id.image_recyclerview);
                adapter = new OnlineOrderAdapter(imageUrlsList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(OnlineOrderActivity.this);
                recyclerView.setAdapter(adapter);
                //리사이클러뷰의 레이아웃 매니저를 LinearLayoutManager로 설정합니다.
                recyclerView.setLayoutManager(layoutManager);

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
}
