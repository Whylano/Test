package com.example.myapplication.model;

public class Reborn {

    private String productImgUrl;
    private String detailImgUrl;
    private String goodsName;

    public Reborn(String productImgUrl, String detailImgUrl, String goodsName) {
        this.productImgUrl = productImgUrl;
        this.detailImgUrl = detailImgUrl;
        this.goodsName = goodsName;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getDetailImgUrl() {
        return detailImgUrl;
    }

    public void setDetailImgUrl(String detailImgUrl) {
        this.detailImgUrl = detailImgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

}
