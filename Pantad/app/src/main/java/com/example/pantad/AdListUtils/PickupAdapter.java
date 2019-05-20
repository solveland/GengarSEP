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
    protected ItemDetailsWindow createItemListener(final Ad ad, final RecyclerView.ViewHolder viewHolder,View v) {

        final ItemDetailsWindow itemDetails = new PickupDetailsWindow(v, ad, upm);
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);


        //Riktigt ful lösning, måste gå att göra bättre:
        if(ad.isClaimed()){
            itemDetails.functionButton.setBackgroundColor(Color.RED);
            itemDetails.functionButton.setText("Unclaim");
        }
        // Create and connect listener to claim button
        itemDetails.functionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!ad.isClaimed()) {
                    Snackbar.make(viewHolder.itemView, "Ad has been claimed!", Snackbar.LENGTH_SHORT).show();
                    String recyclerID = Settings.Secure.getString(v.getContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    userModel.claimAd(ad, recyclerID);
                }

                else{
                    Snackbar.make(viewHolder.itemView, "Ad has been unclaimed!", Snackbar.LENGTH_SHORT).show();
                    userModel.unClaimAd(ad);
                }
                itemDetails.dismiss();
            }
        });

        //TODO FIX THIS FUCKING IMAGELOADER
        //ImageLoader.loadImageFromUrl(upm.getViewingPhotoUrl(), itemDetails.userAvatar);
        Snackbar.make(viewHolder.itemView, ad.getDonatorID(), Snackbar.LENGTH_SHORT).show();

        itemDetails.userAvatar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Context mainActivity = viewHolder.itemView.getContext();
                Intent intent = new Intent(mainActivity, UserProfileActivity.class);
                intent.putExtra("uid", ad.getDonatorID());
                mainActivity.startActivity(intent);
            }
        });


        return itemDetails;
    }
/*
    Involves populating data into the item through holder

    The textfields should be modified in order to format our output better!
     */






}
