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
import com.example.pantad.R;
import com.example.pantad.UserModel;

public abstract class ItemDetailsWindow extends PopupWindow {

public Ad ad;
    public Button functionButton;
    public Button cancelButton;
    public UserModel userModel;
    public View parent;

    public ItemDetailsWindow(View parent, Ad ad, UserModel userModel) {
        this.ad=ad;
        this.userModel=userModel;
        this.parent=parent;

        Context context = parent.getContext();
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(context);
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
}
