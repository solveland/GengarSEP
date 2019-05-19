package com.example.pantad.AdListUtils;

import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;
import com.example.pantad.UserModel;

public class PickupDetailsWindow extends ItemDetailsWindow {

    public TextView name;
    public TextView address;
    public TextView value;
    public TextView rating;
    public TextView description;;
    public ImageView userAvatar;


    public PickupDetailsWindow(View parent, Ad ad, UserModel userModel) {
        super(parent, ad,userModel);
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

        // Set all values to attributes
        this.name.setText(ad.getName());
        this.address.setText("Address: " + ad.getAddress());
        this.value.setText("Uppskattat pantvärde: " + ad.getValue() + "kr");
        this.description.setText(ad.getMessage());
        this.rating.setText("" + "4.5" + "/5.0 user rating");


        //Riktigt ful lösning, måste gå att göra bättre:
        if(ad.isClaimed()){
            functionButton.setBackgroundColor(Color.RED);
            functionButton.setText("Unclaim");
        }
        // Create and connect listener to claim button
        functionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!ad.isClaimed()) {
                    Snackbar.make(parent, "Ad has been claimed!", Snackbar.LENGTH_SHORT).show();
                    String recyclerID = Settings.Secure.getString(v.getContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
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
}
