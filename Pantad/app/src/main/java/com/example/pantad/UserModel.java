package com.example.pantad;

import android.arch.lifecycle.ViewModel;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

/*An object of this class is shared between the different fragments, all communication between
    them is handled by this object */
public class UserModel extends ViewModel {
    /* Our list of ads. There might be a more suitable container for this */
    //public final ArrayList<Annons> annonser = new ArrayList<>();

    public final List<Annons> claimedAds = new ArrayList<>();
    public final List<Annons> availableAds = new ArrayList<>();
    public final List<Annons> postedAds = new ArrayList<>();

    public List<Annons> getClaimedAds() {
        return claimedAds;
    }

    public List<Annons> getAvailableAds() {
        return availableAds;
    }

    public List<Annons> getPostedAds() {
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
    public void addAnnons(String name, String adress, int estimatedValue, String message, String donatorID, Timestamp startTime) {
        DocumentReference adsRef = db.collection("ads").document();
        Annons ad = new Annons(name, adress, estimatedValue, message, adsRef.getId(), donatorID, startTime);
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
    }




    public void updateAds(final AnnonsAdapter adap){
        db.collection("ads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    claimedAds.clear();
                    availableAds.clear();
                    postedAds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Annons annons = document.toObject(Annons.class);
                            if (annons.getRecyclerID() != null && annons.getRecyclerID().equals(deviceID)){
                                claimedAds.add(annons);
                            }
                            if (!annons.isClaimed() && !annons.getDonatorID().equals(deviceID)){
                                availableAds.add(annons);
                            }
                            if (annons.getDonatorID().equals(deviceID)){
                                postedAds.add(annons);
                            }

                        }
                    //There is probably a better way of doing this, just want to update the adapter once the task is done
                    adap.notifyDataSetChanged();
                }
            }
        });
    }
    //Vi borde antagligen ändra så att Adapter och Usermodel kopplas ihop med observer-pattern
    public void removeAd(Annons annons){
        db.collection("ads").document(annons.getAdID()).delete();
        claimedAds.remove(annons);
        availableAds.remove(annons);
        postedAds.remove(annons);
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
}