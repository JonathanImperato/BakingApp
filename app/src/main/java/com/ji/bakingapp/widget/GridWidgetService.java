package com.ji.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ji.bakingapp.R;
import com.ji.bakingapp.database.ItemsContract;
import com.ji.bakingapp.utils.Ingredient;

import java.util.ArrayList;


/**
 * Created by jonathanimperato on 08/03/18.
 */
public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


        ArrayList<Ingredient> ingredientsListGrid;
        String latestHeader;
        Context mContext;

        public GridRemoteViewsFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {
            ingredientsListGrid = getIngredients();
        }

        @Override
        public void onDataSetChanged() {
            ingredientsListGrid = getIngredients();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return (ingredientsListGrid != null) ? ingredientsListGrid.size() : 0;
        }

        private ArrayList<Ingredient> getIngredients() {

            Cursor ingredientCursor = mContext.getContentResolver()
                    .query(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                            null,
                            null,
                            null,
                            null);

            ArrayList<Ingredient> ingredients = new ArrayList<>();
            if (ingredientCursor != null) {
                while (ingredientCursor.moveToNext()) {
                    Ingredient ingredient = getIngredientFromCursor(ingredientCursor);
                    ingredients.add(ingredient);
                }
                ingredientCursor.close();
            }


            return ingredients;
        }

        private Ingredient getIngredientFromCursor(Cursor ingredientCursor) {
            Ingredient ingredient = new Ingredient();

            ingredient.setIngredient(ingredientCursor.getString(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_NAME)));
            ingredient.setMeasure(ingredientCursor.getString(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE)));
            ingredient.setQuantity(ingredientCursor.getDouble(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY)));

            String name = ingredientCursor.getString(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMNS_FOOD_NAME)).replace("_", " ");

            if (ingredientCursor.getPosition() == 0 || !name.equals(latestHeader) || latestHeader == null) { // a sort of Section Header to divide foods ingredients
                ingredient.setFoodName(name);
                latestHeader = name;
            }
            return ingredient;
        }


        @Override
        public RemoteViews getViewAt(int i) {

            if (ingredientsListGrid.size() == 0) {
                return null;
            }

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item_layout);

            Ingredient ingredient = ingredientsListGrid.get(i);
            if (ingredient.getFoodName() != null) {
                remoteViews.setTextViewText(R.id.food_name, ingredient.getFoodName());
                remoteViews.setViewVisibility(R.id.food_name, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.food_name_layout, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.food_name, View.GONE);
                remoteViews.setViewVisibility(R.id.food_name_layout, View.GONE);
            }

            remoteViews.setTextViewText(R.id.ingredient_name, ingredientsListGrid.get(i).getIngredient());
            remoteViews.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantity() + " " + ingredient.getMeasure());


            Intent fillInIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.ingredient_widget_layout, fillInIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
