package com.example.pantad.AdListUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;

public class PickupDetailsWindow extends ItemDetailsWindow {

    public TextView name;
    public TextView address;
    public TextView value;
    public TextView rating;
    public TextView description;;
    public ImageView userAvatar;


    public PickupDetailsWindow(View parent, Ad ad) {
        super(parent, ad);
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

        setContentView(popupView);
    }
}
