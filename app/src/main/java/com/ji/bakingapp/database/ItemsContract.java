package com.ji.bakingapp.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jonathanimperato on 09/03/18.
 */

public class ItemsContract {

    /**
     * PROJECT BASED ON UDACITY'S CONTENT PROVIDER PROJECT
     * https://github.com/udacity/android-content-provider
     */
    public static final String CONTENT_AUTHORITY = "com.ji.bakingapp.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class FoodEntry implements BaseColumns {
        // table name
        public static final String TABLE_FOOD = "food";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_FOOD_NAME = "food_name";
        public static final String COLUMN_FOOD_SERVINGS = "servings";

        // create content uri
        public static final Uri CONTENT_URI_FOOD_TABLE = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FOOD).build();

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_FOOD =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FOOD;

        // for building URIs on insertion
        public static Uri buildFoodUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_FOOD_TABLE, id);
        }


    }
    public static class IngredientEntry implements BaseColumns {
        // table name
        public static final String TABLE_INGREDIENTS = "ingredients";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
        public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
        public static final String COLUMN_INGREDIENT_MEASURE = "measure";
        public static final String COLUMNS_FOOD_NAME = "food_name";

        // create content uri
        public static final Uri CONTENT_URI_INGREDIENT_TABLE = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_INGREDIENTS).build();

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_INGREDIENT =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_INGREDIENTS;

        // for building URIs on insertion
        public static Uri buildIngredientUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_INGREDIENT_TABLE, id);
        }

    }
}
