package com.ji.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jonathanimperato on 09/03/18.
 */

public class FoodDBHelper extends SQLiteOpenHelper {
    /**
     * PROJECT BASED ON UDACITY'S CONTENT PROVIDER PROJECT
     * https://github.com/udacity/android-content-provider
     */
    public static final String LOG_TAG = FoodDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public FoodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " +
                ItemsContract.FoodEntry.TABLE_FOOD + "(" + ItemsContract.FoodEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemsContract.FoodEntry.COLUMN_FOOD_NAME +
                " TEXT NOT NULL, " +
                ItemsContract.FoodEntry.COLUMN_FOOD_ID +
                " INTEGER NOT NULL, " +
                ItemsContract.FoodEntry.COLUMN_FOOD_SERVINGS +
                " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FOOD_TABLE);

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " +
                ItemsContract.IngredientEntry.TABLE_INGREDIENTS + "(" + ItemsContract.IngredientEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemsContract.IngredientEntry.COLUMN_INGREDIENT_NAME +
                " TEXT NOT NULL, " +
                ItemsContract.IngredientEntry.FOOD_ID +
                " INTEGER NOT NULL, " +
                ItemsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY +
                " DOUBLE NOT NULL, " +
                ItemsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE +
                " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.w(LOG_TAG, "Upgrading database from version " + i + " to " +
                i1 + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemsContract.FoodEntry.TABLE_FOOD);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                ItemsContract.FoodEntry.TABLE_FOOD + "'");

        // re-create database
        onCreate(sqLiteDatabase);


        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemsContract.IngredientEntry.TABLE_INGREDIENTS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                ItemsContract.IngredientEntry.TABLE_INGREDIENTS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}