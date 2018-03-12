package com.ji.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ji.bakingapp.R;
import com.ji.bakingapp.utils.Ingredient;

import java.util.ArrayList;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class IngredientsAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final ArrayList<Ingredient> ingredients;

    public IngredientsAdapter(Context mContext, ArrayList<Ingredient> ingredients) {
        this.mContext = mContext;
        this.ingredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.ingredient_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String capTitle = ingredients.get(position).getIngredient().substring(0, 1).toUpperCase() + ingredients.get(position).getIngredient().substring(1);//title with first letter capitalized
        ((IngredientsAdapterViewHolder) holder).quantity.setText(ingredients.get(position).getQuantity() + " " + ingredients.get(position).getMeasure());
        ((IngredientsAdapterViewHolder) holder).title.setText(capTitle);
    }

    @Override
    public int getItemCount() {
        return (ingredients == null || ingredients.size() == 0) ? 0 : ingredients.size();
    }


    private class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title, quantity;

        public IngredientsAdapterViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.ingredient_name);
            quantity = view.findViewById(R.id.quantity);
        }

    }
}