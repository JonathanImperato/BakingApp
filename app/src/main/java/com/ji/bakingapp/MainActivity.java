package com.ji.bakingapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;

import com.ji.bakingapp.data.LoadFood;
import com.ji.bakingapp.utils.Food;
import com.ji.bakingapp.widget.FoodAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_recycler)
    RecyclerView mRecyclerView;
    FoodAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO USE SCHEMATIC AND BUTTERKNIFE
        ButterKnife.bind(this);
        int numberOfColumns = getNumberOfColumns();
        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager lM = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(lM);
        getSupportLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<Food[]>() {
            @Override
            public Loader<Food[]> onCreateLoader(int id, Bundle args) {
                LoadFood asyncTaskLoader = new LoadFood(getApplicationContext());
                asyncTaskLoader.forceLoad();
                return asyncTaskLoader;
            }

            @Override
            public void onLoadFinished(Loader<Food[]> loader, Food[] data) {
                mAdapter = new FoodAdapter(MainActivity.this, data);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onLoaderReset(Loader<Food[]> loader) {

            }

        });
    }

    public int getNumberOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberOfCs = (int) (dpWidth / 180);
        return numberOfCs;
    }

}
