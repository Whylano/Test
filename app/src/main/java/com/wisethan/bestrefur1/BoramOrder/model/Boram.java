package com.wisethan.bestrefur1.BoramOrder.model;

public class Boram {

    private final String productImgUrl;

    private final String detailImgUrl;
    private final String goodsName;

    public Boram(String productImgUrl, String detailImgUrl, String goodsName) {
        this.productImgUrl = productImgUrl;
        this.detailImgUrl = detailImgUrl;
        this.goodsName = goodsName;
    }
    public String getGoodsName() {
        return goodsName;
    }


    public String getProductImgUrl() {
        return productImgUrl;
    }


    public String getDetailImgUrl() {
        return detailImgUrl;
    }

}
