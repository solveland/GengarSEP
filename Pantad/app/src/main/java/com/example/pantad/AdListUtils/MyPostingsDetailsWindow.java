package com.example.pantad.AdListUtils;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;

public class MyPostingsDetailsWindow extends ItemDetailsWindow {

    public TextView address;
    public TextView value;
    public TextView description;
    public FloatingActionButton updateBtn;
    public ConstraintLayout claimedAdLayout;


    public MyPostingsDetailsWindow(View parent, Ad ad, UserProfileModel upm,UserModel userModel) {
        super(parent,ad,upm,userModel);
    }

    public void setValues(LayoutInflater inflater){
        View popupView = inflater.inflate(R.layout.myadsitem_details, null);

        this.name = (TextView) popupView.findViewById(R.id.details_name);
        this.address = (TextView) popupView.findViewById(R.id.details_address);
        this.value = (TextView) popupView.findViewById(R.id.details_value);
        this.description = (EditText) popupView.findViewById(R.id.editablemessage_details);
        this.updateBtn=(FloatingActionButton) popupView.findViewById(R.id.updatemessage_details);
        functionButton = (Button) popupView.findViewById(R.id.delete_details);
        cancelButton = (Button) popupView.findViewById(R.id.cancel_details);
        userAvatar = (ImageView) popupView.findViewById(R.id.user_avatar_details);
        claimedAdLayout = (ConstraintLayout) popupView.findViewById(R.id.claimedAd_Layout);


        // Set all values to attributes, will be updated in the future to show what we want it to show (hopefully by a better designer then me)
        if (!ad.isClaimed()) {
            claimedAdLayout.setVisibility(View.GONE);
            ConstraintLayout cLayout = popupView.findViewById(R.id.itemDetailsLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(cLayout);
            constraintSet.connect(address.getId(), ConstraintSet.TOP, R.id.unclaimed_text, ConstraintSet.BOTTOM, 180);
            constraintSet.applyTo(cLayout);
        }
        else {
            // Set viewing profile to the ad claimer, update load recycler avatar and name
            name.setText(" has claimed your ad!");
            upm.updateViewingProfile(ad.getRecyclerID());
            setUserAvatarListener(ad.getRecyclerID());
        }
        this.address.setText("Address: " + ad.getAddress());
        this.value.setText("Uppskattat pantvärde: " + ad.getValue() + "kr");
        this.description.setText(ad.getMessage());
        this.description.setEnabled(false);

        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ad.isClaimed()) {
                    Snackbar.make(v, "A claimed ad cannot be edited!", Snackbar.LENGTH_SHORT).show();
                }
                else if(description.isEnabled()){
                    userModel.updateAdMessage(ad,description.getText().toString());
                    updateBtn.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                    description.setEnabled(false);
                }
                else{
                    description.setEnabled(true);
                    description.setSelected(true);
                    description.requestFocus();
                    InputMethodManager imm = (InputMethodManager) parent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    updateBtn.setImageResource(R.drawable.ic_done_black_24dp);
                }
            }
        });

        functionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userModel.removeAd(ad);
                Snackbar.make(parent, "Ad has been deleted!", Snackbar.LENGTH_SHORT).show();
                dismiss();
            }
        });

        setContentView(popupView);
    }
}
