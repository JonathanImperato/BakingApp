package com.ji.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ji.bakingapp.fragments.StepsFragment;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        if (savedInstanceState == null) {
            StepsFragment stepFragment = new StepsFragment();
            Bundle bundle = getIntent().getExtras();
            ArrayList<Step> food = bundle.getParcelableArrayList("food_step");
            int index = bundle.getInt("stepIndex", 0);
            Log.d(TAG, "Created");
            stepFragment.setStepsList(food);
            stepFragment.setStepIndex(index);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.steps_container, stepFragment)
                    .addToBackStack("backStack")
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
