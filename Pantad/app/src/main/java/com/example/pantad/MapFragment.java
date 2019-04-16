package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;


/**
 *
 * Handles the mapView. Not sure if we are going to have a view just for the map in the end though.
 * Currently a massive Google map initialized in Gothenburg
 *
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    /*
    The coordinates for Gothenburg
     */
    private LatLng first = new LatLng(57.709339, 11.969752);
    private CameraPosition lastPosition = new CameraPosition(first, 10.0f, 0.0f, 0.0f);
    private UserModel userModel;

    /* A geocoder object. A geocoder is supposed to be able to convert an address to a LatLng, not used yet though
     Is created here but sent to the usermodel
     */
    private Geocoder geocoder;

    public MapFragment() {
        // Required empty public constructor
    }

/* Creates a refrence to the usermodel and sets up the map */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        geocoder = new Geocoder(getActivity(), Locale.getDefault()); //Geocoders are used to get position from an address
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);   //The usermodel is a shared object between the framgments, it handles the communication between them
        userModel.setGeocoder(geocoder);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        mapFragment.getMapAsync(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Will position the map in the same position where it was last left
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        userModel.setMap(googleMap);
        userModel.getmMap().moveCamera(CameraUpdateFactory.newCameraPosition(lastPosition));
        userModel.getmMap().addMarker(new MarkerOptions().position(first).title("Hej").snippet("Välkommen till GBG"));

    }

    /* Saves the last viewed location */
    @Override
    public void onPause() {
        super.onPause();
        if(userModel.getmMap()!=null)
        lastPosition = userModel.getmMap().getCameraPosition();
    }
}