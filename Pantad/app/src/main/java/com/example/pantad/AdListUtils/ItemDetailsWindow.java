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
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class ItemDetailsWindow extends PopupWindow implements PropertyChangeListener {

    public Ad ad;
    public Button functionButton;
    public Button cancelButton;
    public UserModel userModel;
    public View parent;
    public ImageView userAvatar;
    private UserProfileModel upm;

    public ItemDetailsWindow(View parent, Ad ad, UserProfileModel upm, UserModel userModel) {
        this.ad=ad;
        this.userModel=userModel;
        this.parent=parent;

        Context context = parent.getContext();
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView;
        setValues(inflater);
        this.upm = upm;
        upm.setObserver(this);



        // Create and connect listener to cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

            setWidth(width);
            setHeight(height);
            setFocusable(true);

    }
    public abstract void setValues(LayoutInflater inflater);

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (userAvatar == null){
            return;
        }
        ImageLoader.loadImageFromUrl(upm.getViewingPhotoUrl(), userAvatar);
    }
}
