package com.example.pantad.AdListUtils;

import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.ImageLoader;
import com.example.pantad.R;
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PickupDetailsWindow extends ItemDetailsWindow{

    public TextView address;
    public TextView value;
    public TextView rating;
    public TextView description;


    public PickupDetailsWindow(View parent, Ad ad, UserProfileModel upm,UserModel userModel) {
        super(parent, ad,upm,userModel);
    }

    @Override
    public void setValues(LayoutInflater inflater) {
        View popupView = inflater.inflate(R.layout.item_details, null);

        this.name = (TextView) popupView.findViewById(R.id.details_name);
        this.address = (TextView) popupView.findViewById(R.id.details_address);
        this.value = (TextView) popupView.findViewById(R.id.details_value);
        this.description = (TextView) popupView.findViewById(R.id.details_description);
        this.rating = (TextView) popupView.findViewById(R.id.user_rating);
        functionButton = (Button) popupView.findViewById(R.id.delete_details);
        cancelButton = (Button) popupView.findViewById(R.id.cancel_details);
        userAvatar = (ImageView) popupView.findViewById(R.id.user_avatar_details);

        // Set viewing profile to the donator, load name and avatar
        upm.updateViewingProfile(ad.getDonatorID());

        // Set user avatar on click listener
        setUserAvatarListener(ad.getDonatorID());

        // Set all values to attributes
        this.name.setText("");  // Empty string, will be loaded from profile later
        this.address.setText("Address: " + ad.getAddress());
        this.value.setText("Uppskattat pantvärde: " + ad.getValue() + "kr");
        this.description.setText(ad.getMessage());
        this.description.setMovementMethod(new ScrollingMovementMethod());

        //Riktigt ful lösning, måste gå att göra bättre:
        if(ad.isClaimed()){
            functionButton.setBackgroundColor(ResourcesCompat.getColor(popupView.getResources(), R.color.colorRed, null));
            functionButton.setText("Unclaim");
        }
        // Create and connect listener to claim button
        functionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!ad.isClaimed()) {
                    Snackbar.make(parent, "Ad has been claimed!", Snackbar.LENGTH_SHORT).show();
                    String recyclerID = upm.getUid();
                    userModel.claimAd(ad, recyclerID);
                }

                else{
                    Snackbar.make(parent, "Ad has been unclaimed!", Snackbar.LENGTH_SHORT).show();
                    userModel.unClaimAd(ad);
                }
                dismiss();
            }
        });

        setContentView(popupView);
    }


    //rating.setText returns nullpointerexception for upm.getViewingProfile().getRating() if i dont do this
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        this.rating.setText(upm.getViewingProfile().getRating()+"/5.0 user rating");
        if (propertyChangeEvent.getPropertyName().equals("viewingProfile")) {
            ImageLoader.loadImageFromUrl(upm.getViewingPhotoUrl(), userAvatar, 250);
            String str = upm.getViewingName() + name.getText();
            name.setText(str);
        }
    }
}
