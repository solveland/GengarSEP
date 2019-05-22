package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.pantad.AdListUtils.ItemDetailsWindow;
import com.example.pantad.AdListUtils.PickupDetailsWindow;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;


/**
 *
 * Handles the mapView. Not sure if we are going to have a view just for the map in the end though.
 * Currently a massive Google map initialized in Gothenburg
 *
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, PropertyChangeListener {
    /*
    The coordinates for Gothenburg
     */
    private LatLng first = new LatLng(57.709339, 11.969752);
    private CameraPosition lastPosition = new CameraPosition(first, 10.0f, 0.0f, 0.0f);
    private UserModel userModel;
    private UserProfileModel upm;

    /* A geocode object. A geocode is supposed to be able to convert an address to a LatLng, not used yet though
     Is created here but sent to the userModel
     */
    private Geocoder geocoder;

    private ToggleButton claimedToggle;
    private ToggleButton availableToggle;
    private ToggleButton stationsToggle;

    public MapFragment() {

    }

    private class ToggleButtonListener implements CompoundButton.OnCheckedChangeListener {

        private MapFragment parent;

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            paintFilterButton(buttonView,isChecked);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
            mapFragment.getMapAsync(parent);
        }

        public ToggleButtonListener(MapFragment parent){
            this.parent = parent;
        }
    }

    private void paintFilterButton(CompoundButton buttonView, boolean isChecked){
        if (isChecked) {
            buttonView.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
            buttonView.setTextColor(Color.WHITE);
        } else {
            buttonView.setBackgroundColor(0);
            buttonView.setTextColor(Color.BLACK);
        }
    }



/* Creates a refrence to the usermodel and sets up the map */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        /*geocoder = new Geocoder(getActivity(), Locale.getDefault()); //Geocoder are used to get position from an address
        userModel.setGeocoder(geocoder);*/
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);   //The userModel is a shared object between the fragments, it handles the communication between them
        userModel.setObserver(this);
        upm = ViewModelProviders.of(getActivity()).get(UserProfileModel.class);

        stationsToggle = rootView.findViewById(R.id.stationsToggle);
        availableToggle = rootView.findViewById(R.id.availableToggle);
        claimedToggle = rootView.findViewById(R.id.claimedToggle);
        ToggleButtonListener listener = new ToggleButtonListener(this);
        for(ToggleButton button : new ToggleButton[]{stationsToggle,availableToggle,claimedToggle}){
            button.setOnCheckedChangeListener(listener);
            paintFilterButton(button,button.isChecked());
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SupportMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

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
        if (userModel.getmMap() == null){
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(lastPosition));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(userModel.getmMap().getCameraPosition()));
        }
        userModel.setMap(googleMap);

        googleMap.clear();
        if (stationsToggle.isChecked()) {
            MapDecorator.addPantStations(googleMap, getActivity().getApplicationContext());
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        MapDecorator.addAdsToMap(googleMap,userModel,availableToggle.isChecked(),claimedToggle.isChecked());
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Ad ad = (Ad)marker.getTag();
                if (ad == null){
                    return false;
                }

                createItemListener(ad,getView());
                return true;
            }
        });
    }



    //TODO: This is copy pasted from pickupfragment, can probably make this more abstract
    protected void createItemListener(final Ad ad, View v) {
        if (!ItemDetailsWindow.canOpenDetailView()){
            return;
        }
        final ItemDetailsWindow itemDetails = new PickupDetailsWindow(v, ad,upm,userModel);
        itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Dim the background
        View container = itemDetails.getContentView().getRootView();
        Context context = itemDetails.getContentView().getContext();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) container.getLayoutParams();
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.4f;
        wm.updateViewLayout(container, params);

    }

    /* Saves the last viewed location */
    @Override
    public void onPause() {
        super.onPause();
        if(userModel.getmMap()!=null)
        lastPosition = userModel.getmMap().getCameraPosition();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!isAdded()){
            return;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(this);
    }
}

