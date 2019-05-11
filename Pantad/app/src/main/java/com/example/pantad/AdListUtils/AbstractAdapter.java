package com.example.pantad.AdListUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pantad.R;
import com.example.pantad.UserModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class AbstractAdapter extends RecyclerView.Adapter implements PropertyChangeListener {

    public SectionedAdListContainer adContainer;
    public UserModel userModel;

    public AbstractAdapter(SectionedAdListContainer adContainer, UserModel userModel) {
        this.adContainer = adContainer;
        this.userModel=userModel;
        userModel.setObserver(this);
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

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
}
