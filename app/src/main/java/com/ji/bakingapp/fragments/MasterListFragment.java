package com.ji.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.ji.bakingapp.R;
import com.ji.bakingapp.SummaryActivity;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class MasterListFragment extends Fragment {

    // Define a new interface OnStepSelected that triggers a callback in the host activity
    OnStepSelected mCallback;
    private static final String TAG = "MasterListFragment";
    private ArrayList<Step> foodSteps;

    @BindView(R.id.steps_recyclerview)
     RecyclerView stepsRecyclerView;

    public interface OnStepSelected {
        void onStepSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelected");
        }
    }


    public MasterListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        ButterKnife.bind(this, rootView);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MasterListAdapter mAdapter = null;
        foodSteps = SummaryActivity.food_step;

        if (foodSteps != null)
            mAdapter = new MasterListAdapter(getContext(), SummaryActivity.food_step, new MasterListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    mCallback.onStepSelected(position);
                }
            });

        Log.d(TAG, "Created");

        stepsRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}
