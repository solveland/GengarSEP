package com.example.pantad;

import android.arch.lifecycle.ViewModel;
import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.example.pantad.AdListUtils.AbstractAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/*An object of this class is shared between the different fragments, all communication between
    them is handled by this object */
public class UserModel extends ViewModel {
    /* Our list of ads. There might be a more suitable container for this */
    //public final ArrayList<Ad> ads = new ArrayList<>();

    public final List<Ad> claimedAds = new ArrayList<>();
    public final List<Ad> availableAds = new ArrayList<>();
    public final List<Ad> postedAds = new ArrayList<>();

    public PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public List<Ad> getClaimedAds() {
        return claimedAds;
    }

    public List<Ad> getAvailableAds() {
        return availableAds;
    }

    public List<Ad> getPostedAds() {
        return postedAds;
    }

    /*The GoogleMap object from our mapFragment. We use this object to set markers or otherwise modify the map
     */
    private GoogleMap mMap;

    private String deviceID;

    /* A geocoder object. A geocoder is supposed to be able to convert an address to a LatLng, not used yet though */
    private Geocoder geocoder;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public UserModel() {
        updateAds();
        // trigger user load.
    }

    void doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
    }

    /*
    Adds another ad to the list of ads
    @param name  The name of the person posting the ad
    @param address The location where the trade will take place
    @param estimatedValue - The estimated value of the pant in whole SEK:s
    */
    public void addAd(String name, String address, int estimatedValue, String message, String donatorID, Timestamp startTime) {
        DocumentReference adsRef = db.collection("ads").document();
        Ad ad = new Ad(name, address, estimatedValue, message, adsRef.getId(), donatorID, startTime);
        adsRef.set(ad);

        if (ad.getRecyclerID() != null && ad.getRecyclerID().equals(deviceID)){
            claimedAds.add(ad);
        }
        if (!ad.isClaimed() && !ad.getDonatorID().equals(deviceID)){
            availableAds.add(ad);
        }
        if (ad.getDonatorID().equals(deviceID)){
            postedAds.add(ad);
        }
        pcs.firePropertyChange(null,true,false);
    }




    public void updateAds(){
        db.collection("ads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    claimedAds.clear();
                    availableAds.clear();
                    postedAds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ad ad = document.toObject(Ad.class);
                            if (ad.getRecyclerID() != null && ad.getRecyclerID().equals(deviceID)){
                                claimedAds.add(ad);
                            }
                            if (!ad.isClaimed() && !ad.getDonatorID().equals(deviceID)){
                                availableAds.add(ad);
                            }
                            if (ad.getDonatorID().equals(deviceID)){
                                postedAds.add(ad);
                            }

                        }
                    //There is probably a better way of doing this, just want to update the adapter once the task is done
                    pcs.firePropertyChange(null,true,false);
                }
            }
        });
    }

    public void removeAd(Ad ad){
        db.collection("ads").document(ad.getAdID()).delete();
        claimedAds.remove(ad);
        availableAds.remove(ad);
        postedAds.remove(ad);
        pcs.firePropertyChange(null,true,false);
    }




    public GoogleMap getmMap() {
        return mMap;
    }

    public void setMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public void setDeviceID(String deviceID){
        this.deviceID = deviceID;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    //Made it so the adapters listen to the usermodel. Makes sure the lists are updated whenever a change is made. Don´t know what this means for
    //our dependencies though, they might be real bad atm.
    public void setObserver(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    public void claimAd(Ad ad, String recyclerID){
        db.collection("ads").document(ad.getAdID()).update("claimed", true);
        db.collection("ads").document(ad.getAdID()).update("recyclerID", recyclerID);
        ad.setClaimed(true);
        availableAds.remove(ad);
        claimedAds.add(ad);
        pcs.firePropertyChange(null,true,false);
    }

    public void unClaimAd(Ad ad){
        db.collection("ads").document(ad.getAdID()).update("claimed", false);
        db.collection("ads").document(ad.getAdID()).update("recyclerID", null);
        ad.setClaimed(false);
        availableAds.add(ad);
        claimedAds.remove(ad);
        pcs.firePropertyChange(null,true,false);
    }
}