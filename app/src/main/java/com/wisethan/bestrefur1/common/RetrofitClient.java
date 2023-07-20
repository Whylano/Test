package com.wisethan.bestrefur1.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // API 호출 및 JSON 데이터 가져오기
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiService.WISETHAN_SERVER_URL) // 서버의 베이스 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public ApiService retrofitAPI = retrofit.create(ApiService.class);
}

