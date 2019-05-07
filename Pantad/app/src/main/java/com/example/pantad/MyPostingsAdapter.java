package com.example.pantad;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings.Secure;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pantad.AdListUtils.SectionedAdListContainer;
import com.google.firebase.firestore.FirebaseFirestore;

/* This class is needed for the recyleview. It connects the textfields in the pos_ad xml file to a list of postings.
    Is used in HomeFragment to create and inflate the Recyclerview.
*/
public class MyPostingsAdapter extends RecyclerView.Adapter {

    private SectionedAdListContainer adContainer;
    private UserModel userModel;

    public MyPostingsAdapter(SectionedAdListContainer adContainer, UserModel userModel) {
        this.adContainer = adContainer;
        this.userModel=userModel;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        RecyclerView.ViewHolder viewHolder;

        if(viewType == 0) {
            // Normal list item
            // Inflate the custom layout
            final View contactView = inflater.inflate(R.layout.recycler_view_item, parent, false);

            // Return a new holder instance
            viewHolder = new AdItemViewHolder(contactView);
        } else {
            // Header
            final View contactView = inflater.inflate(R.layout.recycler_segment_header_item, parent, false);

            // Return a new holder instance
            viewHolder = new SegmentHeaderViewHolder(contactView);
        }

        return viewHolder;

    }

    /**
     * Returns which itemviewtype goes in the specified position of the recyclerview
     * @param pos Position in the recyclerview
     * @return 0 for regular item view, 1 for section header
     */
    @Override
    public int getItemViewType(int pos){
        return (adContainer.isSegment(pos))? 1:0;
    }






    /*
    Involves populating data into the item through holder

    The textfields should be modified in order to format our output better!
     */

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == 0) {
            // Normal ad view item
            // Get the data model based on position
            final Annons annons = adContainer.getAd(position);
            final String name = annons.getName();
            final String address = annons.getAddress();
            final int value = annons.getValue();
            final String message = annons.getMessage();
            final String elapsedTime = TimeUtil.getDifference(annons.getStartTime());
            // Set item views based on your views and data model
            TextView nameView = ((AdItemViewHolder)viewHolder).nameTextView;
            nameView.setText("Namn: " + annons.getName() + "                  " + elapsedTime);

            TextView adressView = ((AdItemViewHolder)viewHolder).adressTextView;
            adressView.setText("Upphämtningsadress: " + annons.getAddress());

            TextView valueView = ((AdItemViewHolder)viewHolder).valueTextView;
            valueView.setText("Uppskattat pantvärde: " + Integer.toString(annons.getValue()) + "kr");

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Create the item details window
                    final ItemDetailsWindow itemDetails = new ItemDetailsWindow(v, name, address, value, 4.5, message);
                    itemDetails.showAtLocation(v, Gravity.CENTER, 0, 0);

                    // Dim the background
                    View container = itemDetails.getContentView().getRootView();
                    Context context = itemDetails.getContentView().getContext();

                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) container.getLayoutParams();
                    params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    params.dimAmount = 0.4f;
                    wm.updateViewLayout(container, params);

                    // Create and connect listener to claim button
                    itemDetails.deleteButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            removeAd(annons);
                            Snackbar.make(viewHolder.itemView, "Ad has been deleted!", Snackbar.LENGTH_SHORT).show();
                            itemDetails.dismiss();
                        }
                    });

                    // Create and connect listener to cancel button
                    itemDetails.cancelButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            itemDetails.dismiss();
                        }
                    });
                }

            });
        } else {
            // Segment header item
            TextView headerText = ((SegmentHeaderViewHolder)viewHolder).headerText;
            headerText.setText(adContainer.getHeaderText(position));
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return adContainer.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class AdItemViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView adressTextView;
        public TextView valueTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public AdItemViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any AdItemViewHolder instance.
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.annons_namn);
            adressTextView = (TextView) itemView.findViewById(R.id.annons_adress);
            valueTextView = (TextView) itemView.findViewById(R.id.annons_value);
         }
    }

    public class SegmentHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerText;

        public SegmentHeaderViewHolder(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header_text);
        }
    }

    private class ItemDetailsWindow extends PopupWindow {
        public TextView name;
        public TextView address;
        public TextView value;
        public TextView rating;
        public TextView description;
        public Button deleteButton;
        public Button cancelButton;
        public ImageView userAvatar;

        public ItemDetailsWindow(View parent, String name, String address, int value, double rating, String description) {
            Context context = parent.getContext();
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LayoutInflater inflater = LayoutInflater.from(context);
            View popupView = inflater.inflate(R.layout.item_details, null);

            // Find reference to each element in the layout
            this.name = (TextView) popupView.findViewById(R.id.details_name);
            this.address = (TextView) popupView.findViewById(R.id.details_address);
            this.value = (TextView) popupView.findViewById(R.id.details_value);
            this.description = (TextView) popupView.findViewById(R.id.details_description);
            this.rating = (TextView) popupView.findViewById(R.id.user_rating);
            deleteButton = (Button) popupView.findViewById(R.id.claim_details);
            deleteButton.setText("Delete");
            deleteButton.setBackgroundColor(Color.RED);

            cancelButton = (Button) popupView.findViewById(R.id.cancel_details);
            userAvatar = (ImageView) popupView.findViewById(R.id.user_avatar_details);

            // Set all values to attributes
            this.name.setText(name);
            this.address.setText("Address: " + address);
            this.value.setText("Uppskattat pantvärde: " + value + "kr");
            this.description.setText(description);
            this.rating.setText("" + rating + "/5.0 user rating");

            setContentView(popupView);
            setWidth(width);
            setHeight(height);
            setFocusable(true);
        }


    }
    public void removeAd(Annons annons) {
        userModel.removeAd(annons);
        this.notifyDataSetChanged();
    }
}
