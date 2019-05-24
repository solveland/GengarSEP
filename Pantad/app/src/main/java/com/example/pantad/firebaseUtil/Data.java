package com.example.pantad.firebaseUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("title")
    @Expose
    public String title;
    public String donatorID;
    public String donatorName;

    public Data() {

    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDonatorID(String donatorID){this.donatorID=donatorID;}
    public void setDonatorName(String name){donatorName=name;}
}
