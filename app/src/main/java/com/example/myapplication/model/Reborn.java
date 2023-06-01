package com.example.myapplication.model;

public class Reborn {
    private String productImgUrl;
    private String goodsName;
    private String detailUrl;
    public Reborn(){}
    public Reborn(String productImgUrl, String goodsName, String detailUrl) {
        this.productImgUrl = productImgUrl;
        this.goodsName = goodsName;
        this.detailUrl = detailUrl;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }


}
