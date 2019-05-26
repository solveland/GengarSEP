package com.example.pantad.AdListUtils;

import android.app.Dialog;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;
import com.example.pantad.firebaseUtil.MessageType;

public class MyPostingsDetailsWindow extends ItemDetailsWindow {

    public TextView address;
    public TextView value;
    public TextView description;
    public FloatingActionButton updateBtn;
    public ConstraintLayout claimedAdLayout;
    private FloatingActionButton completeBtn;

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
            name.setText(" har begärt din annons!");
            upm.updateViewingProfile(ad.getRecyclerID());
            setUserAvatarListener(ad.getRecyclerID());
            createConfirmStuff(popupView);
        }
        this.address.setText("Adress: " + ad.getAddress());
        this.value.setText("Uppskattat pantvärde: " + ad.getValue() + "kr");
        this.description.setText(ad.getMessage());
        this.description.setEnabled(false);

        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ad.isClaimed()) {
                    Snackbar.make(v, "En publicerad annons kan inte redigeras!", Snackbar.LENGTH_SHORT).show();

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
                Snackbar.make(parent, "Annons har tagits bort!", Snackbar.LENGTH_SHORT).show();
                dismiss();
            }
        });

        setContentView(popupView);
    }

    private void createConfirmStuff(final View popupView){
        completeBtn=popupView.findViewById(R.id.completeBtn);

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog rankDialog = new Dialog(parent.getContext());
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                TextView text = rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText("Betygssätt användare " +upm.getViewingName()+" och bekräfta");

                Button confirmButton = rankDialog.findViewById(R.id.rank_dialog_button);
                Button cancelBtn = rankDialog.findViewById(R.id.rank_dialog_cancel);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankDialog.dismiss();
                    }
                });
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if the rating is 0, the user probably just skipped this step. Is this a good solution for this?
                        if(ratingBar.getRating()!=0){
                            upm.updateRating(ad.getRecyclerID(),upm.getViewingProfile(),ratingBar.getRating());
                        }
                        String title = ad.getName() + " har angett att uppdraget är avklarat";
                        userModel.sendNotification(ad, title, "COMPLETED");

                        //Uncomment next line after debugging
                        userModel.removeAd(ad);
                        rankDialog.dismiss();
                        MyPostingsDetailsWindow.this.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
            }
        });
    }
}
