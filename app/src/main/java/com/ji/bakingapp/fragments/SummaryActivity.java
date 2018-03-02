package com.ji.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ji.bakingapp.IngredientsActivity;
import com.ji.bakingapp.R;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanimperato on 01/03/18.
 */


public class SummaryActivity extends AppCompatActivity implements MasterListFragment.OnStepSelected {
    private boolean mTwoPane;
    public static ArrayList<Step> food;
    @BindView(R.id.ingredients_card)
    CardView mCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        food = this.getIntent().getParcelableArrayListExtra("food");
        setContentView(R.layout.activity_summary);
        ButterKnife.bind(this);

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SummaryActivity.this, IngredientsActivity.class));
            }
        });

        mTwoPane = false;
        if (findViewById(R.id.android_me_linear_layout) != null) {
            if (savedInstanceState == null) {

                mTwoPane = true;

                // Change the GridView to space out the images more on tablet
                RecyclerView gridView = (RecyclerView) findViewById(R.id.steps_recyclerview);


                if (savedInstanceState == null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    StepsFragment stepsFragment = new StepsFragment();
                    stepsFragment.setStepsList(food);

                    // Add the fragment to its container using a transaction
                    fragmentManager.beginTransaction()
                            .add(R.id.steps_container, stepsFragment)
                            .commit();


                }
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public void onStepSelected(int position) {
        if (mTwoPane) { //it means it is a tablet

            StepsFragment newFragment = new StepsFragment();
            newFragment.setStepsList(food);
            newFragment.setStepIndex(position);
            // Replace the old head fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_container, newFragment)
                    .commit();

        } else {

            Bundle b = new Bundle();
            b.putInt("stepIndex", position);
            b.putParcelableArrayList("food", food);

            final Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(b);

            startActivity(intent);
        }
    }
}
