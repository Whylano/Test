package com.wisethan.bestrefur1.OnlineOrder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnlineOrderClinet {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";


    public static OnlineOrederService getApiService(){return getInstance().create(OnlineOrederService.class);}

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
