package com.ji.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jonathanimperato on 09/03/18.
 */

public class FoodProvider extends ContentProvider {
    /**
     * PROJECT BASED ON UDACITY'S CONTENT PROVIDER PROJECT
     * https://github.com/udacity/android-content-provider
     **/
    private static final String LOG_TAG = FoodProvider.class.getSimpleName();
    //  private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDBHelper mOpenHelper;


    /*    private static UriMatcher buildUriMatcher() {
            // Build a UriMatcher by adding a specific code to return based on a match
            // It's common to use NO_MATCH as the code for this case.
            final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
            final String authority = ItemsContract.CONTENT_AUTHORITY;

            // add a code for each type of URI you want
            matcher.addURI(authority, ItemsContract.FoodEntry.TABLE_FOOD, FOOD);
            matcher.addURI(authority, ItemsContract.FoodEntry.TABLE_FOOD + "/#", FOOD_WITH_ID);

            return matcher;
        }
    */
    @Override
    public boolean onCreate() {
        mOpenHelper = new FoodDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uri.equals(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE)) { //IT IS THE INGREDIENT INSERT
            Cursor retCursor;
            retCursor = mOpenHelper.getReadableDatabase().query(
                    ItemsContract.IngredientEntry.TABLE_INGREDIENTS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
            return retCursor;
        } else {

            Cursor retCursor;
            retCursor = mOpenHelper.getReadableDatabase().query(
                    ItemsContract.FoodEntry.TABLE_FOOD,
                    projection,
                    selection ,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
            return retCursor;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (uri.equals(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE)) { //IT IS THE INGREDIENT INSERT
            return ItemsContract.IngredientEntry.CONTENT_ITEM_INGREDIENT;
        } else
            return ItemsContract.FoodEntry.CONTENT_ITEM_FOOD;

        /*
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FOOD: {
                return ItemsContract.FoodEntry.CONTENT_ITEM_FOOD;
            }
            case FOOD_WITH_ID: {
                return ItemsContract.FoodEntry.CONTENT_ITEM_FOOD;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }*/
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long _id = 0;

        if (uri.equals(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE)) { //IT IS THE INGREDIENT INSERT

            _id = db.insert(ItemsContract.IngredientEntry.TABLE_INGREDIENTS, null, values);
            if (_id > 0) {
                returnUri = ItemsContract.IngredientEntry.buildIngredientUriWithId(_id);
            } else {
                throw new android.database.SQLException("Failed to insert row into: " + uri);
            }
        } else {
            _id = db.insert(ItemsContract.FoodEntry.TABLE_FOOD, null, values);

            // insert unless it is already contained in the database
            if (_id > 0) {
                returnUri = ItemsContract.FoodEntry.buildFoodUriWithId(_id);
            } else {
                throw new android.database.SQLException("Failed to insert row into: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //    final int match = sUriMatcher.match(uri);
        int numDeleted;

        if (uri.equals(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE)) { //IT IS THE INGREDIENT INSERT

            numDeleted = db.delete(
                    ItemsContract.IngredientEntry.TABLE_INGREDIENTS, selection, selectionArgs);
            // reset _ID
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                    ItemsContract.IngredientEntry.TABLE_INGREDIENTS + "'");
        } else {
            numDeleted = db.delete(
                    ItemsContract.FoodEntry.TABLE_FOOD, selection, selectionArgs);
            // reset _ID
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                    ItemsContract.FoodEntry.TABLE_FOOD + "'");
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
  /*      final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOOD:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(MoviesContract.MoviesEntry.TABLE_MOVIES,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.MoviesEntry.COLUMN_TITLE)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }*/
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }


        numUpdated = db.update(ItemsContract.FoodEntry.TABLE_FOOD,
                contentValues,
                selection,
                selectionArgs);

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
