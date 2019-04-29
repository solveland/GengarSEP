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
 * A simple {@link Fragment} subclass.
 */
public class DonatorFragment extends Fragment {

    private UserModel userModel;
    private RecyclerView donatorRVAnnonser;
    private AnnonsAdapter adapter;

    public DonatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donator, container, false);
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);

        donatorRVAnnonser = rootView.findViewById(R.id.donator_recycler_view);

        // Set layout manager to position the items
        donatorRVAnnonser.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Attach the adapter to the recyclerview to populate items
        donatorRVAnnonser.setAdapter(adapter);
        // That's all!
        return rootView;
    }

}
