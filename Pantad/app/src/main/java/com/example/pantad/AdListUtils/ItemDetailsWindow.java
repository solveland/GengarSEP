package com.example.pantad.AdListUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.ImageLoader;
import com.example.pantad.R;
import com.example.pantad.UserProfileModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class ItemDetailsWindow extends PopupWindow implements PropertyChangeListener {

    public Ad ad;
    public Button functionButton;
    public Button cancelButton;
    public ImageView userAvatar;
    private UserProfileModel upm;

    public ItemDetailsWindow(View parent, Ad ad, UserProfileModel upm) {
        this.ad=ad;
        Context context = parent.getContext();
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView;
        this.upm = upm;
        upm.setObserver(this);

        setValues(inflater);

        // Find reference to each element in the layout
        /*this.name = (TextView) popupView.findViewById(R.id.details_name);
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
*/
            setWidth(width);
            setHeight(height);
            setFocusable(true);

    }
    public abstract void setValues(LayoutInflater inflater);

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        ImageLoader.loadImageFromUrl(upm.getViewingPhotoUrl(), userAvatar);
    }
}
