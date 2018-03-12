package com.ji.bakingapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.DisplayMetrics;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        //click the first item of the main recyclerview
        if (isScreenSw600dp()) { //test for tablet
            onView(allOf(withId(R.id.main_recycler))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.main_recycler))).perform(actionOnItemAtPosition(0, click()));
            //click the first step of the step recyclerview
            onView(allOf(withId(R.id.steps_recyclerview))).check(matches(isDisplayed()));
            //once in the Intro fragment / activity check if the fab is displayed
            onView(allOf(withId(R.id.fab2))).check(matches(isDisplayed()));
        } else {//test for phones
            onView(allOf(withId(R.id.main_recycler))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.main_recycler))).perform(actionOnItemAtPosition(0, click()));
            //click the first step of the step recyclerview
            onView(allOf(withId(R.id.steps_recyclerview))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.steps_recyclerview))).perform(actionOnItemAtPosition(0, click()));
            //once in the Intro fragment / activity check if the fab is displayed
            onView(allOf(withId(R.id.fab))).check(matches(isDisplayed()));

        }
    }

    public boolean isScreenSw600dp() {
        MainActivity mActivity = mActivityTestRule.getActivity();
        boolean isAtablet = mActivity.getResources().getBoolean(R.bool.isTablet);
        return isAtablet;
    }

}
