package com.example.pantad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;


import com.example.pantad.pantMapUtil.PantParser;
import com.example.pantad.pantMapUtil.PantTriplet;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public final class MapDecorator {



    public static void addPantStations(GoogleMap map, Context context){
        List<PantTriplet<Double, Double, String>> stations = createStationTriplet(context);
        Bitmap bitmap = getBitmapFromVectorDrawable(context,R.drawable.ic_trashcan);
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        for(PantTriplet<Double, Double, String> station : stations){
            LatLng coords = new LatLng(station.getLat(), station.getLng());
            map.addMarker(new MarkerOptions().position(coords).title(station.getAddress()).icon(descriptor));
        }
    }

    private static List<PantTriplet<Double, Double, String>> createStationTriplet(Context context){
        PantTriplet<String, String, String> prefixes = new PantTriplet<>("lat=", "lng=", "address=");

        List<PantTriplet<String, String, String>> tmp = PantParser.textIntoTrip(context,"gbgpantstationer.txt",  'l', 'l', 'a', prefixes, '$');
        List<PantTriplet<Double, Double, String>> stations = new ArrayList<>();
        for(PantTriplet<String, String, String> trip : tmp){
            stations.add(new PantTriplet<>(Double.parseDouble(trip.getLat()), Double.parseDouble(trip.getLng()), trip.getAddress()));
        }
        return stations;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable =  AppCompatResources.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}