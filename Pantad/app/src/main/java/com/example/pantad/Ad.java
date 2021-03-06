package com.example.pantad;
import android.support.annotation.Keep;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;


/*The type of object we want the RecycleView (in the home fragment) to show. All of the
    ad-attributes we want to be shown should be listed here
 */
@Keep
public class Ad {
    private String name;
    private String address;
    private int value;
    private String message;
    private boolean claimed;
    private String adID;
    private Timestamp startTime;
    private String donatorID;
    private String recyclerID;
    private String firebaseToken;
    private GeoPoint location;
    private String recyclerFirebaseToken;

    public Ad(String name, String address, int estimatedValue, String message, String adID, String donatorID, Timestamp startTime, String firebaseToken,GeoPoint location,String recyclerFirebaseToken) {

        this.name = name;
        this.address = address;
        this.value=estimatedValue;
        this.message = message;
        claimed = false;
        this.adID = adID;
        this.startTime = startTime;
        this.donatorID = donatorID;
        this.firebaseToken = firebaseToken;
        this.location = location;
    }

    //Constructor without arguments needed for fireBase
    public Ad(){

    }

    public String getFirebaseToken() { return firebaseToken; }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public void setMessage(String message) { this.message = message; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }

    public String getRecyclerFirebaseToken() {
        return recyclerFirebaseToken;
    }

    public void setRecyclerFirebaseToken(String recyclerFirebaseToken) {
        this.recyclerFirebaseToken = recyclerFirebaseToken;
    }
}