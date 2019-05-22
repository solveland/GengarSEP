package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pantad.AdListUtils.AdListWithSectionHeader;
import com.example.pantad.AdListUtils.MyPostingsAdapter;
import com.example.pantad.AdListUtils.SectionedAdListContainer;
import com.example.pantad.addresses.AddressDatabaseHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonatorFragment extends Fragment implements PropertyChangeListener {

    private UserModel userModel;
    private RecyclerView donatorRVAds;
    private MyPostingsAdapter adapter;
    private FloatingActionButton postAdButton;
    private SwipeRefreshLayout swipeRefesh;
    private String regID;
    private UserProfileModel upm;
    public DonatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donator, container, false);
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);
        upm = ViewModelProviders.of(getActivity()).get(UserProfileModel.class);
        postAdButton=rootView.findViewById(R.id.donator_postadbtn);

        postAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogFragment postAdFragment = new PostAdFragment();
                ((PostAdFragment) postAdFragment).setRegID(regID);
                postAdFragment.show(getFragmentManager(), "");
            }
        });

        donatorRVAds = rootView.findViewById(R.id.donator_recycler_view);

        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        donatorRVAds.setLayoutManager(linearLayoutManager);

        //We will probably need to make a new adapter for this list eventually
        AdListWithSectionHeader[] adsToShow = {new AdListWithSectionHeader(userModel.getPostedAds(),"Your posted ads")};
        adapter = new MyPostingsAdapter(new SectionedAdListContainer(adsToShow),userModel, upm);
        // Attach the adapter to the recyclerView to populate items
        donatorRVAds.setAdapter(adapter);
        // That's all!

        swipeRefesh = rootView.findViewById(R.id.swipe_refresh_donator);
        swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userModel.updateAds();
            }
        });
        userModel.setObserver(this);
        return rootView;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        swipeRefesh.setRefreshing(false);
    }
}//DonatorFragment Class
