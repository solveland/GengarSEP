package com.example.pantad;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pantad.AdListUtils.ItemDetailsWindow;

public class NotificationActivity extends AppCompatActivity {

    private UserProfileModel upm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upm = ViewModelProviders.of(NotificationActivity.this).get(UserProfileModel.class);

        NotificationRatingDialog dialog= new NotificationRatingDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }

    public class NotificationRatingDialog extends Dialog {
        public NotificationRatingDialog(Context context) {
            super(context);
            final String donatorID=getIntent().getStringExtra("donatorID");
            String donatorName=getIntent().getStringExtra("donatorName");
            upm.updateViewingProfile(donatorID);

            setContentView(R.layout.rank_dialog_notification);
            setCancelable(true);
            final RatingBar ratingBar = (RatingBar)findViewById(R.id.dialog_ratingbar);
            TextView text = (TextView) findViewById(R.id.rank_dialog_text1);
            text.setText("Betygsätt \n"+donatorName);
            Button confirmButton = (Button) findViewById(R.id.rank_dialog_button);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ratingBar.getRating()!=0){
                        upm.updateRating(donatorID,upm.getViewingProfile(),ratingBar.getRating());
                    }
                    dismiss();
                }
            });
        }
    }
}