package com.ji.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    // Codes for the UriMatcher //////
    private static final int FOOD = 50;
    private static final int FOOD_WITH_ID = 200;

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
        Cursor retCursor;
        retCursor = mOpenHelper.getReadableDatabase().query(
                ItemsContract.FoodEntry.TABLE_FOOD,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return retCursor;
      /*  switch (sUriMatcher.match(uri)) {
            // All Flavors selected
            case FOOD: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ItemsContract.FoodEntry.TABLE_FOOD,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            // Individual flavor based on Id selected
            case FOOD_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ItemsContract.FoodEntry.TABLE_FOOD,
                        projection,
                        ItemsContract.FoodEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }*/
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
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

        long _id = db.insert(ItemsContract.FoodEntry.TABLE_FOOD, null, values);
        // insert unless it is already contained in the database
        if (_id > 0) {
            returnUri = ItemsContract.FoodEntry.buildFoodUriWithId(_id);
        } else {
            throw new android.database.SQLException("Failed to insert row into: " + uri);
        }/*
        switch (sUriMatcher.match(uri)) {
            case FOOD: {
                long _id = db.insert(ItemsContract.FoodEntry.TABLE_FOOD, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = ItemsContract.FoodEntry.buildFoodUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }*/
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //    final int match = sUriMatcher.match(uri);
        int numDeleted;

        numDeleted = db.delete(
                ItemsContract.FoodEntry.TABLE_FOOD, selection, selectionArgs);
        // reset _ID
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                ItemsContract.FoodEntry.TABLE_FOOD + "'");
        /*
        switch (match) {
            case FOOD:
                numDeleted = db.delete(
                        MoviesContract.MoviesEntry.TABLE_MOVIES, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesEntry.TABLE_MOVIES + "'");
                break;
            case FOOD_WITH_ID:
                numDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_MOVIES,
                        MoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesEntry.TABLE_MOVIES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
*/
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
        /*
        switch (sUriMatcher.match(uri)) {
            case FOOD: {
                numUpdated = db.update(MoviesContract.MoviesEntry.TABLE_MOVIES,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FOOD_WITH_ID: {
                numUpdated = db.update(MoviesContract.MoviesEntry.TABLE_MOVIES,
                        contentValues,
                        MoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
*/
        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
