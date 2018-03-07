package com.ji.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ji.bakingapp.fragments.IntroFragment;
import com.ji.bakingapp.fragments.MasterListFragment;
import com.ji.bakingapp.fragments.StepsFragment;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by jonathanimperato on 01/03/18.
 */


public class SummaryActivity extends AppCompatActivity implements MasterListFragment.OnStepSelected {
    private boolean mTwoPane;
    public static ArrayList<Step> food_step;
    ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        food_step = this.getIntent().getParcelableArrayListExtra("food_steps");
        ingredients = this.getIntent().getParcelableArrayListExtra("food_ingredients");
        setContentView(R.layout.activity_summary);
        ButterKnife.bind(this);


        Log.d(this.getClass().getSimpleName(), "Created");
        mTwoPane = false;
        if (findViewById(R.id.fragmentStep) != null) {
            if (savedInstanceState == null) {

                mTwoPane = true;

                if (savedInstanceState == null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //on created activity i just show the first step that is the introductional one
                    IntroFragment firstStep = new IntroFragment();
                    firstStep.setIngredientArrayList(ingredients);
                    firstStep.setStep(food_step.get(0));

                    // Add the fragment to its container using a transaction
                    fragmentManager.beginTransaction()
                            .add(R.id.steps_container, firstStep)
                            .commit();


                }
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public void onStepSelected(int position) {
        if (position == 0) { //it the recipe introductions, where we are going to show ingredients
            if (mTwoPane) { //if it is a tablet than fragment
                IntroFragment newFragment = new IntroFragment();
                newFragment.setIngredientArrayList(ingredients);
                newFragment.setStep(food_step.get(position));
                // Replace the old head fragment with a new one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.steps_container, newFragment)
                        .commit();

            } else //smartphone, so activity
                startActivity(new Intent(SummaryActivity.this, IntroductionActivity.class).putExtra("step", food_step.get(position)).putExtra("food_ingredients", ingredients));
        } else {
            if (mTwoPane) { //it means it is a tablet

                StepsFragment newFragment = new StepsFragment();
                newFragment.setStepsList(food_step);
                newFragment.setStepIndex(position);
                // Replace the old head fragment with a new one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.steps_container, newFragment)
                        .commit();

            } else {

                Bundle b = new Bundle();
                b.putInt("stepIndex", position);
                b.putParcelableArrayList("food_step", food_step);

                final Intent intent = new Intent(this, StepDetailActivity.class);
                intent.putExtras(b);

                startActivity(intent);
            }
        }
    }
}
