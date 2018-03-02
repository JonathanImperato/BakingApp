package com.ji.bakingapp.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ji.bakingapp.R;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.MasterListAdapterViewHolder> {

    // Keeps track of the context and list of images to display
    private Context mContext;
    private ArrayList<Step> mStepsList;
    OnItemClickListener mCallback;
    private int FIRST_ITEM = 1;
    private int LAST_ITEM = 3;
    private int MIDDLE_ITEM = 2;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public MasterListAdapter(Context mContext, ArrayList<Step> mStepsList, OnItemClickListener mCallback) {
        this.mContext = mContext;
        this.mStepsList = mStepsList;
        this.mCallback = mCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return FIRST_ITEM;
        else if (position == mStepsList.size() - 1) return LAST_ITEM;
        else return MIDDLE_ITEM;
    }

    @Override
    public MasterListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = 0;
        if (viewType == FIRST_ITEM) layoutId = R.layout.step_item_first;
        else if (viewType == MIDDLE_ITEM) layoutId = R.layout.step_item_middle;
        else if (viewType == LAST_ITEM) layoutId = R.layout.step_item_last;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        view.setClickable(true);
        return new MasterListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MasterListAdapterViewHolder holder, final int position) {
        String title = mStepsList.get(position).getShortDescription();
        String capTitle = title.substring(0, 1).toUpperCase() + title.substring(1);//title with first letter capitalized
        holder.title.setText(capTitle);
        holder.stepNumber.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }


    public class MasterListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView title, stepNumber;
        View view;

        public MasterListAdapterViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            view = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onItemClickListener(getAdapterPosition());
                }
            });
        }

    }
}
