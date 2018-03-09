package com.ji.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ji.bakingapp.fragments.StepsFragment;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    int index;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> food;
    @BindView(R.id.prev)
    Button previousButton;
    @BindView(R.id.next)
    Button nextButton;
    FragmentManager fragmentManager;
    String food_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        Log.d(TAG, "Created");
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            StepsFragment stepFragment = new StepsFragment();
            Bundle bundle = getIntent().getExtras();
            food = bundle.getParcelableArrayList("food_step");
            index = bundle.getInt("stepIndex", 0);
            ingredients = bundle.getParcelableArrayList("food_ingredients");
            food_name = this.getIntent().getStringExtra("food_name");

            if (food_name.length() > 0)
                this.setTitle(food_name);
            stepFragment.setStepsList(food);
            stepFragment.setStepIndex(index);


            fragmentManager.beginTransaction()
                    .add(R.id.steps_container, stepFragment)
                    .addToBackStack("backStack")
                    .commit();
        }


        checkForButtonVisibility();
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev:
                if (index > 0) {
                    index--;
                    if (index == 0) { //if true it means that i need to go to the intro step
                        index++; //i set the previous index so when i come back and i use the buttons again it will work fine
                        Intent intent = new Intent(this, IntroductionActivity.class);
                        intent.putExtra("step", food.get(0));
                        intent.putExtra("food_ingredients", ingredients);
                        startActivity(intent);
                    } else {
                        StepsFragment newStepFragment = new StepsFragment();
                        newStepFragment.setStepsList(food);
                        newStepFragment.setStepIndex(index);
                        fragmentManager.beginTransaction()
                                .replace(R.id.steps_container, newStepFragment)
                                .addToBackStack("backStack")
                                .commit();

                        checkForButtonVisibility();
                    }
                }
                break;
            case R.id.next:
                if (index < food.size() - 1) {
                    index++;
                    StepsFragment newStepFragment = new StepsFragment();
                    newStepFragment.setStepsList(food);
                    newStepFragment.setStepIndex(index);
                    fragmentManager.beginTransaction()
                            .replace(R.id.steps_container, newStepFragment)
                            .addToBackStack("backStack")
                            .commit();

                    checkForButtonVisibility();
                }

                break;
        }
    }

    void checkForButtonVisibility() {
        if (index == 0) { //first item so need to hide prev button
            previousButton.setVisibility(View.INVISIBLE);
        } else previousButton.setVisibility(View.VISIBLE);
        if (index == food.size() - 1) { //last item so need to hide next button
            nextButton.setVisibility(View.INVISIBLE);
        } else nextButton.setVisibility(View.VISIBLE);
    }
}
