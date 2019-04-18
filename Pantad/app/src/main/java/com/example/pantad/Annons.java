package com.example.pantad;

import java.util.ArrayList;

/*The type of object we want the RecyleView (in the home fragment) to show. All of the
    ad-attributes we want to be shown should be listed here
 */
public class Annons {
    private String name;
    private String address;
    private int value;
    private String message;
    private boolean claimed;

    public Annons(String name,String adress, int estimatedvalue, String message) {
        this.name = name;
        this.address = adress;
        this.value=estimatedvalue;
        this.message = message;
        claimed = false;
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
    public String getMessage(){ return message; }
    public boolean isClaimed() { return claimed; }

    public void setMessage(String message) { this.message = message; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }

    /* Test method, creates a dummy list of postings */
    public static ArrayList<Annons> createAnonnonsList(int numContacts) {
        ArrayList<Annons> annonser = new ArrayList<Annons>();

        for (int i = 1; i <= numContacts; i++) {
            annonser.add(new Annons("Person "+ i, "Framgången 238",i*20, "Hi"));
        }
        return annonser;
    }
}