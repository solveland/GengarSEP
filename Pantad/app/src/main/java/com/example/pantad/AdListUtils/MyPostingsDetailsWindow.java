package com.example.pantad.AdListUtils;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;
import com.example.pantad.UserModel;

public class MyPostingsDetailsWindow extends ItemDetailsWindow {

    public TextView name;
    public TextView address;
    public TextView value;
    public TextView rating;
    public TextView description;;
    public ImageView userAvatar;
    public FloatingActionButton updateBtn;

    public MyPostingsDetailsWindow(View parent, Ad ad, UserModel userModel) {
        super(parent,ad,userModel);
    }

    public void setValues(LayoutInflater inflater){
        View popupView = inflater.inflate(R.layout.myadsitem_details, null);

        this.name = (TextView) popupView.findViewById(R.id.details_name);
        this.address = (TextView) popupView.findViewById(R.id.details_address);
        this.value = (TextView) popupView.findViewById(R.id.details_value);
        this.description = (EditText) popupView.findViewById(R.id.editablemessage_details);
        this.rating = (TextView) popupView.findViewById(R.id.user_rating);
        this.updateBtn=(FloatingActionButton) popupView.findViewById(R.id.updatemessage_details);
        functionButton = (Button) popupView.findViewById(R.id.delete_details);
        cancelButton = (Button) popupView.findViewById(R.id.cancel_details);
        userAvatar = (ImageView) popupView.findViewById(R.id.user_avatar_details);

        // Set all values to attributes, will be updated in the future to show what we want it to show (hopefully by a better designer then me)
        if (!ad.isClaimed()) {
            this.name.setText("Unclaimed!");
        }
        else {
            this.name.setText(ad.getRecyclerID()); //Change to that person´s name when we have the profile system
            this.rating.setText("" + "4.5" + "/5.0 user rating");
            // set userAvatarPicture
        }
        this.address.setText("Address: " + ad.getAddress());
        this.value.setText("Uppskattat pantvärde: " + ad.getValue() + "kr");
        this.description.setText(ad.getMessage());

        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage(description.getText().toString());
            }
        });


        functionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userModel.removeAd(ad);
                Snackbar.make(parent, "Ad has been deleted!", Snackbar.LENGTH_SHORT).show();
                dismiss();
            }
        });

        setContentView(popupView);
    }
}
