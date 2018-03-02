package com.ji.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ji.bakingapp.R;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        if (savedInstanceState == null) {
            StepsFragment stepFragment = new StepsFragment();
            Bundle bundle = getIntent().getExtras();
            ArrayList<Step> food = bundle.getParcelableArrayList("food");
            int index = bundle.getInt("stepIndex",0);

            stepFragment.setStepsList(food);
            stepFragment.setStepIndex(index);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.steps_container, stepFragment)
                    .commit();
        }

    }
}
