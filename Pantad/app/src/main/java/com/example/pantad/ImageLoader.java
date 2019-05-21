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

    public static void loadImageFromUrl(String url, ImageView profilePic, int customRes) {
        String newResUrl = url.replace("s96", "s" + customRes);
        Picasso.get().load(newResUrl).into(profilePic, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }

}
