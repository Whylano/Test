package com.wisethan.bestrefur1.OnlineOrder.DTO;

import com.google.gson.annotations.SerializedName;

public class OnlineOrderDTO {

    @SerializedName("product_img_url")
    private final String onlineOrderImgUrl;

    public OnlineOrderDTO(String onlineOrderImgUrl) {
        this.onlineOrderImgUrl = onlineOrderImgUrl;
    }

    public String getOnlineOrderImgUrl() {
        return onlineOrderImgUrl;
    }

}
