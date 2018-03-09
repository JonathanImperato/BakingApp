package com.ji.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ji.bakingapp.R;
import com.ji.bakingapp.database.ItemsContract;
import com.ji.bakingapp.utils.Food;
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
        Food[] foodList;

        Context mContext;

        public GridRemoteViewsFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            foodList = getFoods();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return (foodList != null) ? foodList.length : 0;
        }

        private ArrayList<Ingredient> getIngredients(long foodId) {
            Uri baseIngredientUri = ItemsContract
                    .IngredientEntry
                    .CONTENT_URI_INGREDIENT_TABLE.buildUpon().build();

            String ingredientUriString = baseIngredientUri.toString() + "/" + foodId;
            Uri ingredientUri = Uri.parse(ingredientUriString);

            Cursor ingredientCursor = mContext.getContentResolver()
                    .query(ingredientUri,
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

        private Food[] getFoods() {
            Uri baseIngredientUri = ItemsContract
                    .FoodEntry
                    .CONTENT_URI_FOOD_TABLE.buildUpon().build();

            String ingredientUriString = baseIngredientUri.toString();
            Uri ingredientUri = Uri.parse(ingredientUriString);

            Cursor ingredientCursor = mContext.getContentResolver()
                    .query(ingredientUri,
                            null,
                            null,
                            null,
                            null);

            Food[] foods = new Food[ingredientCursor.getCount()];
            if (ingredientCursor != null) {
                while (ingredientCursor.moveToNext()) {
                    Food food = getFoodFromCursor(ingredientCursor);
                    foods[ingredientCursor.getPosition()] = (food);
                }
                ingredientCursor.close();
            }
            return foods;
        }

        private Ingredient getIngredientFromCursor(Cursor ingredientCursor) {
            Ingredient ingredient = new Ingredient();

            ingredient.setIngredient(ingredientCursor.getString(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_NAME)));
            ingredient.setMeasure(ingredientCursor.getString(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE)));
            ingredient.setQuantity(ingredientCursor.getDouble(ingredientCursor
                    .getColumnIndex(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY)));
            return ingredient;
        }

        private Food getFoodFromCursor(Cursor foodCursor) {
            Food food = new Food();

            food.setServings(foodCursor.getInt(foodCursor
                    .getColumnIndex(ItemsContract.FoodEntry.COLUMN_FOOD_SERVINGS)));
            food.setName(foodCursor.getString(foodCursor
                    .getColumnIndex(ItemsContract.FoodEntry.COLUMN_FOOD_NAME)));
            return food;
        }

        @Override
        public RemoteViews getViewAt(int i) {

            if (foodList.length == 0) {
                return null;
            }

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item_layout);
            //  String title = ingredientsListGrid.get(i).getIngredient();
            //  String measure = ingredientsListGrid.get(i).getMeasure();
            //  Double quantity = ingredientsListGrid.get(i).getQuantity();
            remoteViews.setTextViewText(R.id.title_food, foodList[i].getName());
            remoteViews.setTextViewText(R.id.servings_food, String.valueOf(foodList[i].getServings()) + " Servings");
            Intent fillInIntent = new Intent();
            //fillInIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.ingredient_widget_layout, fillInIntent);
            return null;
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
            return foodList[i] != null ? foodList[i].getId() : i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
