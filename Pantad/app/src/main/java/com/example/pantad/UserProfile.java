package com.example.pantad;

import java.io.Serializable;

public class UserProfile implements Serializable {

    private String name;
    private String email;
    private String photoUrl;
    private String phoneNumber;


    public UserProfile(){

    }

    public UserProfile(String name, String email, String photoUrl, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl(){ return photoUrl;}



    public String getPhoneNumber(){return phoneNumber;}

}
