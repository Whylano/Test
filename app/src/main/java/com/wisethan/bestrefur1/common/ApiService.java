package com.wisethan.bestrefur1.common;

import com.wisethan.bestrefur1.BoramOrder.DTO.BoramDTO;
import com.wisethan.bestrefur1.OnlineOrder.DTO.OnlineOrderDTO;
import com.wisethan.bestrefur1.RebornOrder.DTO.RebornDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    String  WISETHAN_SERVER_URL = "https://api.inventory.wisethan.com";

    @Multipart
    @POST("/catalogue/daemyung-online/replaceAll")
    Call<ResponseBody> uploadFiles(@Part List<MultipartBody.Part> files);

    @GET("/catalogue/boram/getAll")
    Call<List<BoramDTO>> getAllByBoram();

    @GET("/catalogue/theReborn/getAll")
    Call<List<RebornDTO>> getAllByReborn();
    @GET("/catalogue/daemyung-online/getAll")
    Call<List<OnlineOrderDTO>> getAllByOnlineOrder();
}