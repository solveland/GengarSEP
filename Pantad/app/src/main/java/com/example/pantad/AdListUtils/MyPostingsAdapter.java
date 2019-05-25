package com.example.pantad.AdListUtils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;


/* This class is needed for the recycleView. It connects the textFields in the pos_ad xml file to a list of postings.
    Is used in HomeFragment to create and inflate the RecyclerView.
*/
public class MyPostingsAdapter extends AbstractAdapter {

    public MyPostingsAdapter(SectionedAdListContainer adContainer, UserModel userModel, UserProfileModel upm) {
        super(adContainer, userModel, upm);
    }

    @Override
    protected void setNameField(TextView nameView, Ad ad) {
        if (ad.isClaimed()) {
            nameView.setText("Claimed");
            nameView.setBackgroundColor(ResourcesCompat.getColor(nameView.getResources(), R.color.colorPrimary, null));
        } else {
            nameView.setText("Unclaimed");
            nameView.setBackgroundColor(ResourcesCompat.getColor(nameView.getResources(), R.color.colorRed, null));
        }
    }

    @Override
    protected ItemDetailsWindow createItemListener(final Ad ad, final View v) {
        if (!ItemDetailsWindow.canOpenDetailView()){
            return null;
        }
        final ItemDetailsWindow itemDetails = new MyPostingsDetailsWindow(v, ad,upm,userModel);
        itemDetails.setElevation(20);
        itemDetails.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);
        return itemDetails;
    }


    public void removeAd(Ad ad) {
        userModel.removeAd(ad);
    }
}
