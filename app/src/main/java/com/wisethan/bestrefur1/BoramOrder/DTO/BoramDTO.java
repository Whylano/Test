package com.wisethan.bestrefur1.BoramOrder.DTO;

import com.google.gson.annotations.SerializedName;

public class BoramDTO {
    @SerializedName("goods_name")
    private final String boramName;

    @SerializedName("product_img_url")
    private final String boramImgUrl;

    @SerializedName("detail_img_url")
    private final String boramDetailUrl;

    public BoramDTO(String boramName, String boramImgUrl, String boramDetailUrl) {
        this.boramName = boramName;
        this.boramImgUrl = boramImgUrl;
        this.boramDetailUrl = boramDetailUrl;
    }

    public String getBoramName() {
        return boramName;
    }

    public String getBoramImgUrl() {
        return boramImgUrl;
    }

    public String getBoramDetailUrl() {
        return boramDetailUrl;
    }

}
