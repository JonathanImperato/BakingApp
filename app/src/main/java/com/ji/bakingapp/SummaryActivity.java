package com.ji.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.ji.bakingapp.fragments.IntroFragment;
import com.ji.bakingapp.fragments.MasterListFragment;
import com.ji.bakingapp.fragments.StepsFragment;
import com.ji.bakingapp.utils.Food;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanimperato on 01/03/18.
 */


public class SummaryActivity extends AppCompatActivity implements MasterListFragment.OnStepSelected {
    private boolean mTwoPane;
    public static ArrayList<Step> food_step;
    ArrayList<Ingredient> ingredients;
    @Nullable
    @BindView(R.id.fragmentStep)
    LinearLayout fragmentStepContainer;
    String food_name;
    Food food;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            food_step = this.getIntent().getParcelableArrayListExtra("food_steps");
            food_name = this.getIntent().getStringExtra("food_name");
            ingredients = this.getIntent().getParcelableArrayListExtra("food_ingredients");
            food = this.getIntent().getParcelableExtra("food");
            Log.d(this.getClass().getSimpleName(), "getIntent() != null");
        }
        if (savedInstanceState != null) {
            food_step = savedInstanceState.getParcelableArrayList("list");
            ingredients = savedInstanceState.getParcelableArrayList("food_ingredients");
            Log.d(this.getClass().getSimpleName(), "savedInstanceState != null");
        }
        setContentView(R.layout.activity_summary);
        ButterKnife.bind(this);
        Log.d(this.getClass().getSimpleName(), "Created");
        if (food_name.length() > 0)
            this.setTitle(food_name);
        mTwoPane = false;
        if (fragmentStepContainer != null) {
            mTwoPane = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            //on created activity i just show the first step that is the introductional one
            IntroFragment firstStep = new IntroFragment();
            firstStep.setIngredientArrayList(ingredients);
            firstStep.setStep(food_step.get(0));

            // Add the fragment to its container using a transaction
            fragmentManager.beginTransaction()
                    .add(R.id.steps_container, firstStep)
                    .addToBackStack("backStack")
                    .commit();

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
                        .addToBackStack("backStack")
                        .commit();

            } else //smartphone, so activity
                startActivity(new Intent(SummaryActivity.this, IntroductionActivity.class)
                        .putExtra("step", food_step.get(position))
                        .putExtra("food_ingredients", ingredients)
                        .putExtra("food_name", food_name)
                        .putExtra("food", food));
        } else {
            if (mTwoPane) { //it means it is a tablet

                StepsFragment newFragment = new StepsFragment();
                newFragment.setStepsList(food_step);
                newFragment.setStepIndex(position);
                // Replace the old head fragment with a new one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.steps_container, newFragment)
                        .addToBackStack("backStack")
                        .commit();

            } else {

                Bundle b = new Bundle();
                b.putInt("stepIndex", position);
                b.putParcelableArrayList("food_step", food_step);
                b.putParcelableArrayList("food_ingredients", ingredients);
                b.putString("food_name", food_name);

                final Intent intent = new Intent(this, StepDetailActivity.class);
                intent.putExtras(b);

                startActivity(intent);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", food_step);
        outState.putParcelableArrayList("food_ingredients", ingredients);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        food_step = savedInstanceState.getParcelableArrayList("list");
        ingredients = savedInstanceState.getParcelableArrayList("food_ingredients");
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
