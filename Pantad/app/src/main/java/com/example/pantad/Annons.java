package com.example.pantad;

import java.util.ArrayList;

/*The type of object we want the RecyleView (in the home fragment) to show. All of the
    ad-attributes we want to be shown should be listed here
 */
public class Annons {
    private String name;
    private String address;
    private int value;

    public Annons(String name,String adress, int estimatedvalue) {
        this.name = name;
        this.address = adress;
        this.value=estimatedvalue;
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


    /* Test method, creates a dummy list of postings */
    public static ArrayList<Annons> createAnonnonsList(int numContacts) {
        ArrayList<Annons> annonser = new ArrayList<Annons>();

        for (int i = 1; i <= numContacts; i++) {
            annonser.add(new Annons("Person "+ i, "Framgången 238",i*20));
        }
        return annonser;
    }
}