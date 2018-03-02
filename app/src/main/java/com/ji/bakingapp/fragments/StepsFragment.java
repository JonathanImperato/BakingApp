package com.ji.bakingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ji.bakingapp.R;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    private static final String TAG = "StepsFragment";
    private ArrayList<Step> stepsList;
    private int stepIndex;
    TextView stepNameTextView;

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            stepsList = savedInstanceState.getParcelableArrayList("list");
            stepIndex = savedInstanceState.getInt("index");
        }

        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        stepNameTextView = view.findViewById(R.id.step_name);

        if (stepIndex == -1) stepIndex = 0;
        String stepName = stepsList.get(stepIndex).getDescription();

        if (stepName != null && !stepName.isEmpty())
            stepNameTextView.setText(stepName);
        return view;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public void setStepsList(ArrayList<Step> stepsList) {
        this.stepsList = stepsList;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList("list", (ArrayList<Step>) stepsList);
        currentState.putInt("index", stepIndex);
    }
}
