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
    private UserProfileModel upm;
    private SwipeRefreshLayout refreshLayout;

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
                ((PostAdFragment) postAdFragment).setRegID(userModel.getRegId());
                postAdFragment.show(getFragmentManager(), "");
            }
        });

        donatorRVAds = rootView.findViewById(R.id.donator_recycler_view);

        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        donatorRVAds.setLayoutManager(linearLayoutManager);

        //We will probably need to make a new adapter for this list eventually
        AdListWithSectionHeader[] adsToShow = {new AdListWithSectionHeader(userModel.getPostedAds(),"Dina publicerade annonser")};
        adapter = new MyPostingsAdapter(new SectionedAdListContainer(adsToShow),userModel, upm);
        // Attach the adapter to the recyclerView to populate items
        donatorRVAds.setAdapter(adapter);

        refreshLayout = rootView.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userModel.updateAds();
            }
        });

        userModel.setObserver(this);
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

}//DonatorFragment Class
