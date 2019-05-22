package com.example.pantad;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingDialog extends Dialog {
    public RatingDialog(Context context) {
        super(context);
        setContentView(R.layout.rank_dialog);
        setCancelable(true);
        final RatingBar ratingBar = (RatingBar)findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(0);
        TextView text = (TextView) findViewById(R.id.rank_dialog_text1);
        text.setText("Rate user and confirm");

        Button confirmButton = (Button) findViewById(R.id.rank_dialog_button);
        Button cancelBtn = findViewById(R.id.rank_dialog_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
