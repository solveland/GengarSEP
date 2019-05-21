package com.example.pantad.AdListUtils;

import android.content.Context;
import android.content.Intent;
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
import com.example.pantad.UserProfileActivity;
import com.example.pantad.UserProfileModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class ItemDetailsWindow extends PopupWindow implements PropertyChangeListener {

    public final Ad ad;
    public Button functionButton;
    public Button cancelButton;
    public UserModel userModel;
    public final View parent;
    public ImageView userAvatar;
    public TextView name;
    protected UserProfileModel upm;
    //making sure only one detailview can be open, fix for the problem with pressing on two ads with 2 different fingers opening two detailviews.
    private static boolean open = false;

    public ItemDetailsWindow(final View parent, final Ad ad, UserProfileModel upm, UserModel userModel) {
        open = true;
        this.ad=ad;
        this.userModel=userModel;
        this.parent=parent;
        this.upm = upm;
        upm.setObserver(this);

        Context context = parent.getContext();
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView;
        setValues(inflater);

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
        if (propertyChangeEvent.getPropertyName().equals("viewingProfile")) {
            ImageLoader.loadImageFromUrl(upm.getViewingPhotoUrl(), userAvatar, 250);
            String str = upm.getViewingName() + name.getText();
            name.setText(str);
        }
    }

    protected void setUserAvatarListener(final String userID) {
        userAvatar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Context mainActivity = parent.getContext();
                Intent intent = new Intent(mainActivity, UserProfileActivity.class);
                intent.putExtra("uid", userID);
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public void dismiss(){
        open = false;
        super.dismiss();
    }

    public static boolean canOpenDetalView(){
        return !open;
    }


}
