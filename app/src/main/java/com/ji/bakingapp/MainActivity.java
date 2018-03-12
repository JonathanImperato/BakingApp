package com.ji.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.ji.bakingapp.adapters.FoodAdapter;
import com.ji.bakingapp.data.LoadFood;
import com.ji.bakingapp.utils.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_recycler)
    RecyclerView mRecyclerView;
    FoodAdapter mAdapter;
    Food[] foodData;
    int TASK_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        int numberOfColumns = getNumberOfColumns();
        mRecyclerView.setHasFixedSize(true);
        //  StaggeredGridLayoutManager lM = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager lM = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(lM);
        if (isInternetAvailable()) {
            if (savedInstanceState != null) {
                foodData = (Food[]) savedInstanceState.getParcelableArray("list");
                mAdapter = new FoodAdapter(MainActivity.this, foodData);
                mRecyclerView.setAdapter(mAdapter);
            } else
                getSupportLoaderManager().initLoader(TASK_ID, null, new LoaderManager.LoaderCallbacks<Food[]>() {
                    @Override
                    public Loader<Food[]> onCreateLoader(int id, Bundle args) {
                        LoadFood asyncTaskLoader = new LoadFood(getApplicationContext());
                        asyncTaskLoader.forceLoad();
                        return asyncTaskLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<Food[]> loader, Food[] data) {
                        foodData = data;
                        mAdapter = new FoodAdapter(MainActivity.this, data);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onLoaderReset(Loader<Food[]> loader) {

                    }

                });
        }
    }

    public int getNumberOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberOfCs = (int) (dpWidth / 180);
        return numberOfCs;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray("list", foodData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        foodData = (Food[]) savedInstanceState.getParcelableArray("list");
    }

    boolean isInternetAvailable() {

        /**
         * FOLLOWED THIS LINK:
         * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
         * ACCORDING TO https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.3omxhyonl2o1
         * CODE AUTHOR: STACKOVERFLOW USER GAR
         */

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
