package com.example.pantad;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class UserProfile implements Serializable {

    private String name;
    private String email;
    private String photoUrl;
    private String phoneNumber;

    public int nrOfRatings;
    public float rating;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserProfile(){

    }

    public UserProfile(String name, String email, String photoUrl, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        nrOfRatings=0;
        rating=0;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl(){ return photoUrl;}

    public String getPhoneNumber(){return phoneNumber;}


    public int getNrOfRatings() {
        return nrOfRatings;
    }

    public float getRating() {
        return rating;
    }

    public void setNrOfRatings(int nrOfRatings) {
        this.nrOfRatings = nrOfRatings;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
