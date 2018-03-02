package com.ji.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ji.bakingapp.R;
import com.ji.bakingapp.fragments.SummaryActivity;
import com.ji.bakingapp.utils.Food;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class FoodAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final Food[] mFoodList;

    public FoodAdapter(Context mContext, Food[] mFoodList) {
        this.mContext = mContext;
        this.mFoodList = mFoodList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.food_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        view.setClickable(true);
        return new FoodAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return (mFoodList == null || mFoodList.length == 0) ? 0 : mFoodList.length;
    }


    private class FoodAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail;

        public FoodAdapterViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.img);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Food food = mFoodList[adapterPosition];
            mContext.startActivity(new Intent(mContext, SummaryActivity.class).putExtra("food_ingredients", food.getIngredients()).putExtra("food_steps", food.getSteps()));
        }
    }
}