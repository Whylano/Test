package com.wisethan.bestrefur1.RebornOrder.DTO;

import com.google.gson.annotations.SerializedName;

public class RebornDTO {

    @SerializedName("goods_name")
    private final String rebornName;

    @SerializedName("product_img_url")
    private final String rebornImgUrl;

    @SerializedName("detail_img_url")
    private final String rebornDetailUrl;
    public RebornDTO(String rebornName, String rebornImgUrl, String rebornDetailUrl) {
        this.rebornName = rebornName;
        this.rebornImgUrl = rebornImgUrl;
        this.rebornDetailUrl = rebornDetailUrl;
    }

    public String getRebornName() {
        return rebornName;
    }

    public String getRebornImgUrl() {
        return rebornImgUrl;
    }

    public String getRebornDetailUrl() {
        return rebornDetailUrl;
    }

}
