package com.ji.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jonathanimperato on 09/03/18.
 */

public class IngredientDBHelper extends SQLiteOpenHelper {
    /**
     * PROJECT BASED ON UDACITY'S CONTENT PROVIDER PROJECT
     * https://github.com/udacity/android-content-provider
     */
    public static final String LOG_TAG = IngredientDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public IngredientDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
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

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.w(LOG_TAG, "Upgrading database from version " + i + " to " +
                i1 + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemsContract.IngredientEntry.TABLE_INGREDIENTS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                ItemsContract.IngredientEntry.TABLE_INGREDIENTS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}