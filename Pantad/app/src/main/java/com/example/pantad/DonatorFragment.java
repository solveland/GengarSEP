package com.example.pantad;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pantad.AdListUtils.AdListWithSectionHeader;
import com.example.pantad.AdListUtils.SectionedAdListContainer;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonatorFragment extends Fragment {

    private UserModel userModel;
    private RecyclerView donatorRVAnnonser;
    private AnnonsAdapter adapter;
    private FloatingActionButton postAdButton;

    public DonatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donator, container, false);
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);
        postAdButton=rootView.findViewById(R.id.donator_postadbtn);

        postAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogFragment postAdFragment = new PostAdFragment();
                postAdFragment.show(getFragmentManager(), "missiles");
            }
        });

        donatorRVAnnonser = rootView.findViewById(R.id.donator_recycler_view);

        // Set layout manager to position the items
        donatorRVAnnonser.setLayoutManager(new LinearLayoutManager(getActivity()));

        //We will probably need to make a new adapter for this list eventually
        AdListWithSectionHeader[] adsToShow = {new AdListWithSectionHeader(userModel.getPostedAds(),"Your posted ads")};
        adapter = new AnnonsAdapter(new SectionedAdListContainer(adsToShow));
        // Attach the adapter to the recyclerview to populate items
        donatorRVAnnonser.setAdapter(adapter);
        // That's all!
        return rootView;
    }


}//DonatorFragment Class
