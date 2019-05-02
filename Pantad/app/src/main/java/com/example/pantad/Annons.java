package com.example.pantad;
import android.support.annotation.Keep;

import com.google.firebase.Timestamp;



/*The type of object we want the RecyleView (in the home fragment) to show. All of the
    ad-attributes we want to be shown should be listed here
 */
@Keep
public class Annons {
    private String name;
    private String address;
    private int value;
    private String message;
    private boolean claimed;
    private String adID;
    private Timestamp startTime;
    private String donatorID;
    private String recyclerID;

    public Annons(String name,String adress, int estimatedvalue, String message, String adID, String donatorID, Timestamp startTime) {

        this.name = name;
        this.address = adress;
        this.value=estimatedvalue;
        this.message = message;
        claimed = false;
        this.adID = adID;
        this.startTime = startTime;
        this.donatorID = donatorID;

    }

    //Constructor without arguments needed for firebase
    public Annons(){

    }


    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public int getValue() { return value; }

    public String getAdID() { return adID; }
    public String getDonatorID () {return donatorID;}
    public String getRecyclerID () {return recyclerID;}
    public String getMessage(){ return message; }

    public Timestamp getStartTime() { return startTime; }

    public boolean isClaimed() { return claimed; }

    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public void setMessage(String message) { this.message = message; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }

}