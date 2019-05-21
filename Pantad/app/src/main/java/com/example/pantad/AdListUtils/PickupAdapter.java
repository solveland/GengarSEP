package com.example.pantad.AdListUtils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;

import com.example.pantad.Ad;

import com.example.pantad.ImageLoader;
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileActivity;
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
    protected ItemDetailsWindow createItemListener(final Ad ad, View v) {
        if (!ItemDetailsWindow.canOpenDetalView()){
            return null;
        }
        final ItemDetailsWindow itemDetails = new PickupDetailsWindow(v, ad, upm,userModel);
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);



        return itemDetails;
    }
/*
    Involves populating data into the item through holder

    The textfields should be modified in order to format our output better!
     */
}
