package com.example.pantad.firebaseUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRequestData {
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
