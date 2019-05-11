package com.example.pantad.AdListUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pantad.R;

public class ItemDetailsWindow extends PopupWindow {
    public TextView name;
    public TextView address;
    public TextView value;
    public TextView rating;
    public TextView description;
    public Button functionButton;
    public Button cancelButton;
    public ImageView userAvatar;

    public ItemDetailsWindow(View parent, String name, String address, int value, double rating, String description) {
        Context context = parent.getContext();
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.item_details, null);

        // Find reference to each element in the layout
        this.name = (TextView) popupView.findViewById(R.id.details_name);
        this.address = (TextView) popupView.findViewById(R.id.details_address);
        this.value = (TextView) popupView.findViewById(R.id.details_value);
        this.description = (TextView) popupView.findViewById(R.id.details_description);
        this.rating = (TextView) popupView.findViewById(R.id.user_rating);
        functionButton = (Button) popupView.findViewById(R.id.claim_details);
        cancelButton = (Button) popupView.findViewById(R.id.cancel_details);
        userAvatar = (ImageView) popupView.findViewById(R.id.user_avatar_details);

        // Set all values to attributes
        this.name.setText(name);
        this.address.setText("Address: " + address);
        this.value.setText("Uppskattat pantvärde: " + value + "kr");
        this.description.setText(description);
        this.rating.setText("" + rating + "/5.0 user rating");

        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        setFocusable(true);
    }
}
