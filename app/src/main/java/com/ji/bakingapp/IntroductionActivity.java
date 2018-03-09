package com.ji.bakingapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ji.bakingapp.adapters.IngredientsAdapter;
import com.ji.bakingapp.database.ItemsContract;
import com.ji.bakingapp.utils.Food;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FOLLOWED UDACITY'S LESSONS MEDIA PLAYBACK PROJECT
 */
public class IntroductionActivity extends AppCompatActivity implements ExoPlayer.EventListener {
    @BindView(R.id.card)
    CardView mCardView;

    @BindView(R.id.ingredientsRecyclerview)
    RecyclerView ingredientsRecyclerview;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private String TAG = this.getClass().getSimpleName();
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static MediaSessionCompat mMediaSession;
    Dialog mFullScreenDialog;
    boolean mExoPlayerFullscreen;
    private ImageView mFullScreenIcon;
    private FrameLayout mFullScreenButton;
    ArrayList<Ingredient> food_ingredients;
    Food food;
    int foodID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ingredientsRecyclerview.setNestedScrollingEnabled(false);
        ingredientsRecyclerview.setLayoutManager(linearLayoutManager);

        food_ingredients = getIntent().getParcelableArrayListExtra("food_ingredients");
        Step step = getIntent().getParcelableExtra("step");
        food = getIntent().getParcelableExtra("food");
        foodID = food.getId();
        IngredientsAdapter adapter = new IngredientsAdapter(this, food_ingredients);
        ingredientsRecyclerview.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update widget
                //UpdateWidgetService.startBakingService(IntroductionActivity.this, food_ingredients);
                if (!isFavourite()) {
                    insertData();
                    Snackbar.make(fab, R.string.recipe_added_to_widget, Snackbar.LENGTH_LONG).setAction("action", null).show();
                } else {
                    removeData();
                    Snackbar.make(fab, R.string.recipe_removed_to_widget, Snackbar.LENGTH_LONG).setAction("action", null).show();
                }
            }
        });


        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.ic_play_arrow));

        // Initialize the Media Session.
        initializeMediaSession();
        initializePlayer(Uri.parse(step.getVideoURL()));
    }

    /**
     * CONTENT PROVIDER STUFF
     */
    public void insertData() {
        ContentValues foodValues = new ContentValues();
        foodValues.put(ItemsContract.FoodEntry.COLUMN_FOOD_NAME, food.getName());
        foodValues.put(ItemsContract.FoodEntry.COLUMN_FOOD_SERVINGS, food.getServings());
        foodValues.put(ItemsContract.FoodEntry.COLUMN_FOOD_ID, foodID);

        insertDataIngredients();
        this.getContentResolver().insert(ItemsContract.FoodEntry.CONTENT_URI_FOOD_TABLE,
                foodValues);
    }

    void insertDataIngredients() {
        int foodId = foodID;
        for (Ingredient ingredient : food_ingredients) { //TODO IMPLEMENT BULK INSERT
            ContentValues foodValues = new ContentValues();
            foodValues.put(ItemsContract.IngredientEntry.FOOD_ID, foodId);
            foodValues.put(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            foodValues.put(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredient());
            foodValues.put(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());

            this.getContentResolver().insert(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                    foodValues);
        }
    }

    void removeData() {
        removeIngredients();
        this.getContentResolver().delete(ItemsContract.FoodEntry.CONTENT_URI_FOOD_TABLE, ItemsContract.FoodEntry.COLUMN_FOOD_ID + " = " + foodID, null);
    }

    void removeIngredients() {
        this.getContentResolver().delete(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE, ItemsContract.IngredientEntry.FOOD_ID + " = " + foodID, null);
    }

    boolean isFavourite() {
        Cursor c = this.getContentResolver().query(
                ItemsContract.FoodEntry.CONTENT_URI_FOOD_TABLE,
                null,
                ItemsContract.FoodEntry.COLUMN_FOOD_ID + " = " + foodID,
                null,
                null);
        return c.getCount() > 0;

    }

    /**
    * CONTENT PROVIDER STUFF FINISHES HERE
    **/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mExoPlayerFullscreen)
                openFullscreenMode(); //enter full screen if changes orientation to landscape
        } else {
            if (mExoPlayerFullscreen) //on rotation change, if after rotated it is vertical
                closeFullscreenMode(); //leave the full screen
        }
    }

    /**
     * FOLLOWED TUTORIAL BY Geoff Ledak
     * URL: https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
     */
    private void initFullscreenMode() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenMode();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenMode() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(IntroductionActivity.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenMode() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(IntroductionActivity.this, R.drawable.ic_fullscreen_expand));
    }

    private void initFullscreenButton() {
        SimpleExoPlayerView controlView = mPlayerView.findViewById(R.id.playerView);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenMode();
                else
                    closeFullscreenMode();
            }
        });
    }

    /**
     * FINISHES HERE
     */

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);


    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false); //default it does not start automatically
        }
    }

    private void releasePlayer() {

        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFullscreenMode();
        initFullscreenButton();
    }
}
