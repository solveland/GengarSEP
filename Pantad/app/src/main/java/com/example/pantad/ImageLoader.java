package com.example.pantad;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public final class ImageLoader {

    public static void loadImageFromUrl(String url, ImageView profilePic) {
        Picasso.get().load(url).into(profilePic, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }
}
