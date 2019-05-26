package com.example.pantad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;


import com.example.pantad.pantMapUtil.PantParser;
import com.example.pantad.pantMapUtil.PantStation;
import com.example.pantad.pantMapUtil.PantTriplet;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public final class MapDecorator {




    public static void addPantStations(GoogleMap map, Context context){
        PantStation[] stations = PantStation.getStations(context);
        Bitmap bitmap = getBitmapFromVectorDrawable(context,R.drawable.ic_pantstation);
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        for(PantStation station : stations){
            LatLng coords = new LatLng(station.lat, station.lon);
            map.addMarker(new MarkerOptions().position(coords).title(station.title).snippet(station.address).icon(descriptor));
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

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth()/3,
                drawable.getIntrinsicHeight()/3, Bitmap.Config.ARGB_8888);  // Divide by 3 becuase image source is too big
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void addAdsToMap(GoogleMap map,UserModel userModel,boolean renderAvailable, boolean renderClaimed){
        List<Ad> adsToRender = new ArrayList<>();
        if (renderAvailable) {
            adsToRender.addAll(userModel.getAvailableAds());
        }
        if (renderClaimed) {
            adsToRender.addAll(userModel.getClaimedAds());
        }
        for(Ad a : adsToRender){
            Marker cur = map.addMarker(new MarkerOptions()
                    .position(new LatLng(a.getLocation().getLatitude(),a.getLocation().getLongitude()))
                    .title(a.getName())
                    .icon(a.isClaimed()? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN):BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) ));
            cur.setTag(a);
        }
    }

}
