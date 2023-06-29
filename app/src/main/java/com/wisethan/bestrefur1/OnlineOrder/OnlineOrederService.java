package com.wisethan.bestrefur1.OnlineOrder;

import com.wisethan.bestrefur1.OnlineOrder.model.OnlineOrderUrl;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface OnlineOrederService {
    @Multipart
    @POST("/daemyung-online/replaceAll")
    Call<OnlineOrderUrl> request(@PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> files);

}

