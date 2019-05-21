package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pantad.AdListUtils.PickupAdapter;
import com.example.pantad.AdListUtils.AdListWithSectionHeader;
import com.example.pantad.AdListUtils.SectionedAdListContainer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 *
 * Contains the RecycleView (aka the list of postings)
 */
public class PickupFragment extends Fragment implements PropertyChangeListener {
    private RecyclerView rvAds;
    private UserModel userModel;
    private SwipeRefreshLayout refreshLayout;
    private PickupAdapter adapter;
    private UserProfileModel upm;


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
        upm = ViewModelProviders.of(getActivity()).get(UserProfileModel.class);

        rvAds = rootView.findViewById(R.id.recyclerView);
        refreshLayout = rootView.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userModel.updateAds();
            }
        });

        userModel.setObserver(this);

        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAds.setLayoutManager(linearLayoutManager);

        // Create adapter passing in the sample user data
        AdListWithSectionHeader[] adsToShow = {new AdListWithSectionHeader(userModel.getClaimedAds(),"Your claimed ads"),new AdListWithSectionHeader(userModel.getAvailableAds(),"Available ads")};
        adapter = new PickupAdapter(new SectionedAdListContainer(adsToShow),userModel, upm);
        // Attach the adapter to the recyclerView to populate items
        rvAds.setAdapter(adapter);

        // That's all!
        return rootView;

    }

    /*
     Gets called when the local list of ads has been updated.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshLayout.setRefreshing(false);
    }

    /*
    Updates the list of postings in recycleView when the home-tab is switched to.
    Might be a better way to do this
     */
   /*
    removed for now, we call upon updateAds() when creating the userModel instead.
    @Override
    public void onResume() {
        super.onResume();
        userModel.updateAds();
        //adapter.notifyDataSetChanged(); //this happens in updateAds currently, might change

    } */
}
