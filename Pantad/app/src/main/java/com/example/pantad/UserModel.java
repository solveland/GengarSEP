package com.example.pantad;

import android.arch.lifecycle.ViewModel;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pantad.AdListUtils.AbstractAdapter;
import com.example.pantad.firebaseUtil.Data;
import com.example.pantad.firebaseUtil.PostRequestData;
import com.example.pantad.pantMapUtil.AddressDatabase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public AddressDatabase dbHelper;

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
    public void addAd(String name, String address, int estimatedValue, String message, String donatorID, Timestamp startTime, String regID) {
        DocumentReference adsRef = db.collection("ads2").document();
        Ad ad = new Ad(name, address, estimatedValue, message, adsRef.getId(), donatorID, startTime, regID);
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
        db.collection("ads2").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        db.collection("ads2").document(ad.getAdID()).delete();
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
        db.collection("ads2").document(ad.getAdID()).update("claimed", true);
        db.collection("ads2").document(ad.getAdID()).update("recyclerID", recyclerID);
        ad.setClaimed(true);
        availableAds.remove(ad);
        claimedAds.add(ad);
        pcs.firePropertyChange(null,true,false);
        sendNotification(ad);
    }

    public void unClaimAd(Ad ad){
        db.collection("ads2").document(ad.getAdID()).update("claimed", false);
        db.collection("ads2").document(ad.getAdID()).update("recyclerID", null);
        ad.setClaimed(false);
        availableAds.add(ad);
        claimedAds.remove(ad);
        pcs.firePropertyChange(null,true,false);
    }

    private void sendNotification(Ad ad) {

        Gson gson = new Gson();
        Data data = new Data();
        data.setTitle("Your ad has been claimed");
        PostRequestData postRequestData = new PostRequestData();
        postRequestData.setTo(ad.getFirebaseToken());
        postRequestData.setData(data);
        String json = gson.toJson(postRequestData);
        String url = "https://fcm.googleapis.com/fcm/send";
        System.out.println(json);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "key=AAAAkNlQVNc:APA91bHHE6Dkyj9fn0bopSkAx0ruyIOU0dDjf6cGaUeqPyGRkf6HF47hCAiWrAmLtSoEHJEyytjGTLjS8Ry67-vyeB3_tOcRY2MSatG3axYdiGadD3dRkrF0T7RGTo3wlQzyYwEKkEqR")
                .post(body)
                .build();


        Callback responseCallBack = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("Fail Message", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("response", response.toString());
            }


        };
        okhttp3.Call call = client.newCall(request);
        call.enqueue(responseCallBack);
    }
}