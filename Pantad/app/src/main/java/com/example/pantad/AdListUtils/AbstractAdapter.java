package com.example.pantad.AdListUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pantad.Ad;
import com.example.pantad.R;
import com.example.pantad.TimeUtil;
import com.example.pantad.UserModel;
import com.example.pantad.UserProfileModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public abstract class AbstractAdapter extends RecyclerView.Adapter implements PropertyChangeListener {

    protected SectionedAdListContainer adContainer;
    protected UserModel userModel;
    protected UserProfileModel upm;

    public AbstractAdapter(SectionedAdListContainer adContainer, UserModel userModel, UserProfileModel upm) {
        this.adContainer = adContainer;
        this.userModel=userModel;
        this.upm = upm;
        userModel.setObserver(this);

    }


    public void propertyChange(PropertyChangeEvent event){
        notifyDataSetChanged();
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

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return adContainer.size();
    }

    public class SegmentHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerText;

        public SegmentHeaderViewHolder(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header_text);
        }
    }

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


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == 0) {
            // Normal ad view item
            // Get the data model based on position
            final Ad ad = adContainer.getAd(position);
            final String elapsedTime = TimeUtil.getDifference(ad.getStartTime());
            // Set item views based on your views and data model
            TextView nameView = ((AdItemViewHolder)viewHolder).nameTextView;
            nameView.setText("Namn: " + ad.getName() + "                  " + elapsedTime);

            TextView addressView = ((AdItemViewHolder)viewHolder).addressTextView;
            addressView.setText("Upphämtningsadress: " + ad.getAddress());

            TextView valueView = ((AdItemViewHolder)viewHolder).valueTextView;
            valueView.setText("Uppskattat pantvärde: " + Integer.toString(ad.getValue()) + "kr");

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    upm.updateViewingProfile(ad.getDonatorID());
                    // Create the item details window
                    final ItemDetailsWindow itemDetails=createItemListener(ad,v);

                    // Dim the background
                    View container = itemDetails.getContentView().getRootView();
                    Context context = itemDetails.getContentView().getContext();

                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) container.getLayoutParams();
                    params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    params.dimAmount = 0.4f;
                    wm.updateViewLayout(container, params);


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
    protected abstract ItemDetailsWindow createItemListener(final Ad ad,final View v);

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class AdItemViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView addressTextView;
        public TextView valueTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public AdItemViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any AdItemViewHolder instance.
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.annons_namn);
            addressTextView = (TextView) itemView.findViewById(R.id.annons_adress);
            valueTextView = (TextView) itemView.findViewById(R.id.annons_value);
        }
    }
}
