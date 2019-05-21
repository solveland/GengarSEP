package com.example.pantad.AdListUtils;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.pantad.Ad;
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
            nameView.setBackgroundColor(Color.parseColor("#0DDB74"));
        } else {
            nameView.setText("Unclaimed");
            nameView.setBackgroundColor(Color.parseColor("#E45454"));
        }
    }

    @Override
    protected ItemDetailsWindow createItemListener(final Ad ad, final View v) {
        if (!ItemDetailsWindow.canOpenDetailView()){
            return null;
        }
        final ItemDetailsWindow itemDetails = new MyPostingsDetailsWindow(v, ad,upm,userModel);
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);
        return itemDetails;
    }


    public void removeAd(Ad ad) {
        userModel.removeAd(ad);
    }
}
