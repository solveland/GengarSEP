package com.example.pantad;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class UserProfileModel extends ViewModel implements Serializable {

    private String uid;
    private UserProfile currentProfile;
    private UserProfile viewingProfile;

    public PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void setUid(String uid){
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public String getName (){return currentProfile.getName();}
    public String getPhotoUrl (){ return currentProfile.getPhotoUrl();}

    public String getViewingName(){return viewingProfile.getName();}
    public String getViewingPhotoUrl (){ return viewingProfile.getPhotoUrl();}
    public UserProfile getViewingProfile (){
        return viewingProfile;
    }


    public void updateCurrentProfile(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("userProfile").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    UserProfile up = document.toObject(UserProfile.class);
                    setCurrentProfile(up);
                    if (up != null) {
                        pcs.firePropertyChange("currentProfile", true, false);
                    }

                } else {

                }
            }
        });
    }



    public void updateViewingProfile(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("userProfile").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Document found in the offline cache
                        DocumentSnapshot document = task.getResult();
                        UserProfile up = document.toObject(UserProfile.class);
                        setViewingProfile(up);
                        System.out.println("test");
                        if (up != null) {
                            pcs.firePropertyChange("viewingProfile", true, false);
                        }
                    } else {
                        Log.w("user profile model", "Viewingprofile query failed");
                    }
                    try {
                    } catch (Exception e){

                    }
                }
            });




    }

    private void setCurrentProfile(UserProfile userProfile){this.currentProfile = userProfile;}

    private void setViewingProfile(UserProfile viewingProfile){this.viewingProfile = viewingProfile;}

    public void setPhoneNumber(String phoneNumber){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("userProfile").document(uid);
        docRef.update("phoneNumber", phoneNumber);
    }

    public void setObserver(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }


    public void updateRating(String uid, UserProfile profile,float newInput) {
        //Unsure about this, should we use the local values or get them from firebase?
        float current=profile.getRating();
        int numberofRatings=profile.getNrOfRatings();
        current=(current*numberofRatings+newInput)/(numberofRatings+1);
        profile.setRating(current);
        profile.setNrOfRatings(numberofRatings+1);
        //add firebase update
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("userProfile").document(uid);
        docRef.update("nrOfRatings", numberofRatings+1);
        docRef.update("rating", current);
    }
}
