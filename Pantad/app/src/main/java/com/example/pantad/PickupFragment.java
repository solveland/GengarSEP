package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pantad.AdListUtils.AdAdapter;
import com.example.pantad.AdListUtils.AdListWithSectionHeader;
import com.example.pantad.AdListUtils.SectionedAdListContainer;


/**
 *
 * Contains the RecycleView (aka the list of postings)
 */
public class PickupFragment extends Fragment {
    private RecyclerView rvAds;
    private UserModel userModel;
    private AdAdapter adapter;

    public PickupFragment() {
        // Required empty public constructor
    }

/*
Handles the setup for the recyclerView
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pickup, container, false);
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);

        rvAds = rootView.findViewById(R.id.recyclerView);

        // Set layout manager to position the items
        rvAds.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create adapter passing in the sample user data
        AdListWithSectionHeader[] adsToShow = {new AdListWithSectionHeader(userModel.getClaimedAds(),"Your claimed ads"),new AdListWithSectionHeader(userModel.getAvailableAds(),"Available ads")};
        adapter = new AdAdapter(new SectionedAdListContainer(adsToShow),userModel);
        // Attach the adapter to the recyclerView to populate items
        rvAds.setAdapter(adapter);
        // That's all!
        return rootView;

    }

    /*
    Updates the list of postings in recycleView when the home-tab is switched to.
    Might be a better way to do this
     */
   /*
    removed for now
    @Override
    public void onResume() {
        super.onResume();
        userModel.updateAds();
        //adapter.notifyDataSetChanged(); //this happens in updateAds currently, might change

    } */
}
