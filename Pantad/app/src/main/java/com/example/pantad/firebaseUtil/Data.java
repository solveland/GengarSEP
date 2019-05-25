package com.example.pantad.firebaseUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("messageType")
    @Expose
    public String messageType;
    @SerializedName("donatorID")
    @Expose
    public String donatorID;
    @SerializedName("donatorName")
    @Expose
    public String donatorName;

    public Data() {

    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDonatorID(String donatorID){this.donatorID=donatorID;}
    public void setDonatorName(String name){donatorName=name;}

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
