package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 *
 * Contains the RecyleView (aka the list of postings)
 */
public class PickupFragment extends Fragment {
    private RecyclerView rvAnnonser;
    private UserModel userModel;
    private AnnonsAdapter adapter;

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

        rvAnnonser = rootView.findViewById(R.id.recyclerView);

        // Set layout manager to position the items
        rvAnnonser.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create adapter passing in the sample user data
        adapter = new AnnonsAdapter(userModel.getAnnonser());
        // Attach the adapter to the recyclerview to populate items
        rvAnnonser.setAdapter(adapter);
        // That's all!
        return rootView;
    }

    /*
    Updates the list of postings in recyleView when the home-tab is switched to.
    Might be a better way to do this
     */
    @Override
    public void onResume() {
        super.onResume();
        userModel.updateAds(adapter);
        //adapter.notifyDataSetChanged(); //this happens in updateAds currently, might change

    }
}
