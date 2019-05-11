package com.example.pantad.AdListUtils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.AdListUtils.AbstractAdapter;
import com.example.pantad.AdListUtils.SectionedAdListContainer;
import com.example.pantad.R;
import com.example.pantad.TimeUtil;
import com.example.pantad.UserModel;


/* This class is needed for the recycleView. It connects the textFields in the pos_ad xml file to a list of postings.
    Is used in HomeFragment to create and inflate the RecyclerView.
*/
public class MyPostingsAdapter extends AbstractAdapter {

    public MyPostingsAdapter(SectionedAdListContainer adContainer, UserModel userModel) {
        super(adContainer, userModel);
    }



    @Override
    protected void modifyItemListener(final ItemDetailsWindow itemDetails,final Ad ad, final RecyclerView.ViewHolder viewHolder) {
        itemDetails.functionButton.setText("Delete");
        itemDetails.functionButton.setBackgroundColor(Color.RED);

        itemDetails.functionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeAd(ad);
                Snackbar.make(viewHolder.itemView, "Ad has been deleted!", Snackbar.LENGTH_SHORT).show();
                itemDetails.dismiss();
            }
        });
    }


    public void removeAd(Ad ad) {
        userModel.removeAd(ad);
    }
}
