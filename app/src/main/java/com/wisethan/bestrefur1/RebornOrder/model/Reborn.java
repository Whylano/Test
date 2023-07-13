package com.wisethan.bestrefur1.RebornOrder.model;

public class Reborn {

    private final String productImgUrl;
    private final String detailImgUrl;
    private final String goodsName;

    public Reborn(String productImgUrl, String detailImgUrl, String goodsName) {
        this.productImgUrl = productImgUrl;
        this.detailImgUrl = detailImgUrl;
        this.goodsName = goodsName;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public String getDetailImgUrl() {
        return detailImgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }


}
