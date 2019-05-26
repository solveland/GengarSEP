package com.example.pantad;

import android.arch.lifecycle.ViewModel;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pantad.firebaseUtil.Data;
import com.example.pantad.firebaseUtil.MessageType;
import com.example.pantad.firebaseUtil.PostRequestData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


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

    private final List<Ad> claimedAds = new ArrayList<>();
    private final List<Ad> availableAds = new ArrayList<>();
    private final List<Ad> postedAds = new ArrayList<>();

    private static final String adCollectionString = "ads2";

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String regId;

    private static final double meterLimitForAd = 5000;


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
    private CameraPosition mapCameraPosition;
    private Location location;


    private String profileID;

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
    public void addAd(String name, String address, int estimatedValue, String message, String donatorID, Timestamp startTime, String regID, GeoPoint location) {
        DocumentReference adsRef = db.collection(adCollectionString).document();
        Ad ad = new Ad(name, address, estimatedValue, message, adsRef.getId(), donatorID, startTime, regID,location,null);
        adsRef.set(ad);

        if (ad.getRecyclerID() != null && ad.getRecyclerID().equals(profileID)){
            claimedAds.add(ad);
        }
        if (!ad.isClaimed() && !ad.getDonatorID().equals(profileID)){
            availableAds.add(ad);
        }
        if (ad.getDonatorID().equals(profileID)){
            postedAds.add(ad);
        }
        pcs.firePropertyChange(null,true,false);
    }




    public void updateAds(){
        db.collection(adCollectionString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    claimedAds.clear();
                    availableAds.clear();
                    postedAds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ad ad = document.toObject(Ad.class);
                            if (ad.getRecyclerID() != null && ad.getRecyclerID().equals(profileID)){
                                claimedAds.add(ad);
                            }
                            if (!ad.isClaimed() && !ad.getDonatorID().equals(profileID) && (location == null || DistanceUtil.getDistanceDouble(location.getLatitude(), ad.getLocation().getLatitude(), location.getLongitude(), ad.getLocation().getLongitude(), 0.0, 0.0 ) < meterLimitForAd)){
                                availableAds.add(ad);
                            }
                            if (ad.getDonatorID().equals(profileID)){
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
        db.collection(adCollectionString).document(ad.getAdID()).delete();
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

    public void setProfileID(String profileID){
        this.profileID = profileID;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public CameraPosition getMapCameraPosition() {
        return mapCameraPosition;
    }

    public void setMapCameraPosition(CameraPosition mapCameraPosition) {
        this.mapCameraPosition = mapCameraPosition;
    }



    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }
    public void setLocation(Location location) {this.location = location; }
    public Location getLocation(){ return location;}

    //Made it so the adapters listen to the usermodel. Makes sure the lists are updated whenever a change is made. Don´t know what this means for
    //our dependencies though, they might be real bad atm.
    public void setObserver(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

       public void claimAd(Ad ad, String recyclerID){
        db.collection(adCollectionString).document(ad.getAdID()).update("claimed", true);
        db.collection(adCollectionString).document(ad.getAdID()).update("recyclerFirebaseToken",regId);
        db.collection(adCollectionString).document(ad.getAdID()).update("recyclerID", recyclerID);
        ad.setClaimed(true);
        ad.setRecyclerFirebaseToken(regId);
        availableAds.remove(ad);
        claimedAds.add(ad);
        pcs.firePropertyChange(null,true,false);
        sendNotification(ad, "Någon har begärt din pant", "CLAIMED");
    }

    public void unClaimAd(Ad ad){
        db.collection(adCollectionString).document(ad.getAdID()).update("claimed", false);
        db.collection(adCollectionString).document(ad.getAdID()).update("recyclerFirebaseToken",null);
        db.collection(adCollectionString).document(ad.getAdID()).update("recyclerID", null);
        ad.setClaimed(false);
        ad.setRecyclerFirebaseToken(null);
        availableAds.add(ad);
        claimedAds.remove(ad);
        pcs.firePropertyChange(null,true,false);
        sendNotification(ad, "Någon har tagit bort sin begäran gällande din pant", "UNCLAIMED");

    }

    public void sendNotification(Ad ad, String title, String messageType) {

        Gson gson = new Gson();
        Data data = new Data();
        data.setMessageType(messageType);
        data.setTitle(title);
        if(messageType.equals("COMPLETED") || messageType.equals("REMOVED")){
            data.setDonatorID(ad.getDonatorID());
            data.setDonatorName(ad.getName());
        }
        PostRequestData postRequestData = new PostRequestData();
        if(messageType.equals("CLAIMED") || messageType.equals("UNCLAIMED")) {
            postRequestData.setTo(ad.getFirebaseToken());
        }else if(messageType.equals("COMPLETED") || messageType.equals("REMOVED")){
            postRequestData.setTo(ad.getRecyclerFirebaseToken());
        }
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

    public void updateAdMessage(Ad ad, String message){
        ad.setMessage(message);
        db.collection(adCollectionString).document(ad.getAdID()).update("message", message);
    }



    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}
