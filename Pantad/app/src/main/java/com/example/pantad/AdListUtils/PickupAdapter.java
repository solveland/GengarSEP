package com.example.pantad.AdListUtils;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.pantad.Ad;

import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;


/* This class is needed for the recycleView. It connects the textFields in the pos_ad xml file to a list of postings.
    Is used in HomeFragment to create and inflate the RecyclerView.
*/
public class PickupAdapter extends AbstractAdapter {


    public PickupAdapter(SectionedAdListContainer adContainer, UserModel userModel, UserProfileModel upm) {
        super( adContainer,  userModel, upm);
    }
    // Usually involves inflating a layout from XML and returning the holder

    @Override
    protected void setNameField(TextView nameView, Ad ad) {
        String str = "Donator: " + ad.getName();
        nameView.setText(str);
    }

    @Override
    protected ItemDetailsWindow createItemListener(final Ad ad, View v) {
        if (!ItemDetailsWindow.canOpenDetailView()){
            return null;
        }
        final ItemDetailsWindow itemDetails = new PickupDetailsWindow(v, ad, upm,userModel);
        itemDetails.setElevation(20);
        itemDetails.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);



        return itemDetails;
    }
/*
    Involves populating data into the item through holder

    The textfields should be modified in order to format our output better!
     */
}
