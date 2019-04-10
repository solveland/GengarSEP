package com.example.pantad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/* This class is needed for the recyleview. It connects the textfields in the pos_ad xml file to a list of postings.
    Is used in HomeFragment to create and inflate the Recyclerview.
*/
public class AnnonsAdapter extends RecyclerView.Adapter<AnnonsAdapter.ViewHolder> {

    private List<Annons> anonnser;

    public AnnonsAdapter(List<Annons> anonnser) {
        this.anonnser = anonnser;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public AnnonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    /*
    Involves populating data into the item through holder

    The textfields should be modified in order to format our output better!
     */

    @Override
    public void onBindViewHolder(AnnonsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Annons annons = anonnser.get(position);

        // Set item views based on your views and data model
        TextView nameView = viewHolder.nameTextView;
        nameView.setText("Namn: "+annons.getName());

        TextView adressView = viewHolder.adressTextView;
        adressView.setText("Upphämtningsadress: "+annons.getAddress());

        TextView valueView = viewHolder.valueTextView;
        valueView.setText("Uppskattat pantvärde: "+ Integer.toString(annons.getValue())+"kr");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return anonnser.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView adressTextView;
        public TextView valueTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.annons_namn);
            adressTextView = (TextView) itemView.findViewById(R.id.annons_adress);
            valueTextView = (TextView) itemView.findViewById(R.id.annons_value);
        }
    }
    }
