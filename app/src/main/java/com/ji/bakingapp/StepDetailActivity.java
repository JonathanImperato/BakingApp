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
import com.ji.bakingapp.utils.Food;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    int index;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> food_step;
    @BindView(R.id.prev)
    Button previousButton;
    @BindView(R.id.next)
    Button nextButton;
    FragmentManager fragmentManager;
    String food_name;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        Log.d(TAG, "Created");
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            food_step = bundle.getParcelableArrayList("food_step");
            food = bundle.getParcelable("food");
            index = bundle.getInt("stepIndex", 0);
            ingredients = bundle.getParcelableArrayList("food_ingredients");
            food_name = food.getName();

        } else {
            food_step = savedInstanceState.getParcelableArrayList("food_step");
            food = savedInstanceState.getParcelable("food");
            ingredients = savedInstanceState.getParcelableArrayList("food_ingredients");
            food_name = food.getName();
            index = savedInstanceState.getInt("index");
        }
        StepsFragment stepFragment = new StepsFragment();
        if (food_name.length() > 0)
            this.setTitle(food_name);
        stepFragment.setStepsList(food_step);
        stepFragment.setStepIndex(index);


        fragmentManager.beginTransaction()
                .add(R.id.steps_container, stepFragment)
                .addToBackStack("backStack")
                .commit();


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
                        intent.putExtra("food_ingredients", ingredients);
                        intent.putExtra("food", food);
                        intent.putExtra("food_step", food_step);
                        startActivity(intent);
                    } else {
                        StepsFragment newStepFragment = new StepsFragment();
                        newStepFragment.setStepsList(food_step);
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
                if (index < food_step.size() - 1) {
                    index++;
                    StepsFragment newStepFragment = new StepsFragment();
                    newStepFragment.setStepsList(food_step);
                    newStepFragment.setStepIndex(index);
                    fragmentManager.beginTransaction()
                            .replace(R.id.steps_container, newStepFragment)
                            .addToBackStack("backStack")
                            .commit();

                    checkForButtonVisibility();
                } else {
                    finish();
                }

                break;
        }
    }

    void checkForButtonVisibility() {
        if (index == food_step.size() - 1) { //last item so need to hide next button
            nextButton.setText(R.string.finish);
        } else { //last item so need to hide next button
            nextButton.setText(R.string.next);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putParcelableArrayList("food_step", food_step);
        outState.putParcelable("food", food);
        outState.putParcelableArrayList("food_ingredients", ingredients);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt("index");
        food_step = savedInstanceState.getParcelableArrayList("food_step");
        food = savedInstanceState.getParcelable("food");
        ingredients = savedInstanceState.getParcelableArrayList("food_ingredients");
    }
}
