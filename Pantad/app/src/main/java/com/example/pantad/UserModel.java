package com.example.pantad;

import android.arch.lifecycle.ViewModel;
import android.location.Geocoder;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/*An object of this class is shared between the different fragments, all communication between
    them is handled by this object */
public class UserModel extends ViewModel {
    /* Our list of ads. There might be a more suitable container for this */
    public final ArrayList<Annons> annonser = new ArrayList<>();

    /*The GoogleMap object from our mapFragment. We use this object to set markers or otherwise modify the map
     */
    private GoogleMap mMap;

    /* A geocoder object. A geocoder is supposed to be able to convert an address to a LatLng, not used yet though */
    private Geocoder geocoder;

    public UserModel() {
        // trigger user load.
    }

    void doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
    }

    /*
    Adds another annons to the list of annonser
    @param name  The name of the person posting the ad
    @param adress The location where the trade will take place
    @param estimatedValue - The estimated value of the pant in whole SEK:s
    */
    public void addAnnons(String name, String adress, int estimatedValue) {
        annonser.add(new Annons(name, adress, estimatedValue));
    }

    public ArrayList getAnnonser() {
        return annonser;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }
}