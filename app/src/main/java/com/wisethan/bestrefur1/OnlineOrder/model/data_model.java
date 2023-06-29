package com.wisethan.bestrefur1.OnlineOrder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class data_model {

    @SerializedName("body")
    @Expose
    private String body;

    public String getBody(){
        return body;
    }
}

