package com.ji.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ji.bakingapp.R;
import com.ji.bakingapp.SummaryActivity;
import com.ji.bakingapp.utils.Food;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodAdapterViewHolder> {
    private final Context mContext;
    private final Food[] mFoodList;

    public FoodAdapter(Context mContext, Food[] mFoodList) {
        this.mContext = mContext;
        this.mFoodList = mFoodList;
    }

    @Override
    public FoodAdapter.FoodAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.food_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        view.setClickable(true);
        FoodAdapterViewHolder holder = new FoodAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FoodAdapterViewHolder holder, int position) {
        holder.title.setText(mFoodList[position].getName());
        holder.servings.setText(mFoodList[position].getServings() + " Servings");
        Drawable img = null;
        String imgUrl = mFoodList[position].getImage();
        if (img != null && !imgUrl.isEmpty() && imgUrl.length() > 0) //it means that there is an image
        {
            holder.thumbnail.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(imgUrl).into(holder.thumbnail);
        } else {
            /*switch (position) {
                case 0:
                    img = mContext.getResources().getDrawable(R.drawable.nutella_pie);
                    break;
                case 1:
                    img = mContext.getResources().getDrawable(R.drawable.brownies);
                    break;
                case 2:
                    img = mContext.getResources().getDrawable(R.drawable.yellow_cake);
                    break;
                case 3:
                    img = mContext.getResources().getDrawable(R.drawable.cheese_cake);
                    break;
                default:
                    img = mContext.getResources().getDrawable(R.drawable.nutella_pie);
                    break;
            }
            */
            holder.thumbnail.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (mFoodList == null || mFoodList.length == 0) ? 0 : mFoodList.length;
    }


    public class FoodAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RoundedImageView thumbnail;
        TextView title, servings;

        public FoodAdapterViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.img);
            title = view.findViewById(R.id.title_food);
            servings = view.findViewById(R.id.servings_food);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Food food = mFoodList[adapterPosition];
            mContext.startActivity(new Intent(mContext, SummaryActivity.class)
                    .putExtra("food_ingredients", food.getIngredients())
                    .putExtra("food_steps", food.getSteps())
                    .putExtra("food", food));
        }

    }
}