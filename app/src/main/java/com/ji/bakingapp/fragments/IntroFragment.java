package com.ji.bakingapp.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.ji.bakingapp.R;
import com.ji.bakingapp.adapters.IngredientsAdapter;
import com.ji.bakingapp.database.ItemsContract;
import com.ji.bakingapp.utils.Food;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanimperato on 02/03/18.
 */

public class IntroFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;

    private static MediaSessionCompat mMediaSession;

    private ImageView mFullScreenIcon;
    private FrameLayout mFullScreenButton;
    private static final String TAG = "IntroFragment";
    private Step step;
    public long videoPosition;
    private Food food;
    private ArrayList<Ingredient> ingredientArrayList;

    @BindView(R.id.ingredientsRecyclerview)
    RecyclerView ingredientsRecyclerview;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.fab2)
    FloatingActionButton fab;

    public IntroFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            ingredientArrayList = savedInstanceState.getParcelableArrayList("list");
            step = savedInstanceState.getParcelable("step");
            videoPosition = savedInstanceState.getLong("videoPos");
            food = savedInstanceState.getParcelable("food");
            Log.d(TAG,"onActivityCreated savedInstanceState valye " + videoPosition);
        }

        View view = inflater.inflate(R.layout.fragment_introduction, container, false);
        ButterKnife.bind(this, view);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource (getResources(), R.drawable.ic_play_arrow));
        // Initialize the Media Session.
        if (step != null && !TextUtils.isEmpty(step.getVideoURL())) {
            initializeMediaSession();
            initializePlayer(Uri.parse(step.getVideoURL()));
            Log.d(TAG,"valye " + videoPosition);
        }
        mFullScreenIcon = mPlayerView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = mPlayerView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setVisibility(View.GONE);
        mFullScreenIcon.setVisibility(View.GONE);


        correctFabImg();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavourite()) {
                    insertDataIngredients();
                    correctFabImg();
                    Snackbar.make(mPlayerView, R.string.recipe_added_to_widget, Snackbar.LENGTH_LONG).setAction("action", null).show();
                } else {
                    removeIngredients();
                    correctFabImg();
                    Snackbar.make(mPlayerView, R.string.recipe_removed_to_widget, Snackbar.LENGTH_LONG).setAction("action", null).show();
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerview.setNestedScrollingEnabled(false);
        ingredientsRecyclerview.setLayoutManager(linearLayoutManager);
        IngredientsAdapter adapter = new IngredientsAdapter(getContext(), ingredientArrayList);
        ingredientsRecyclerview.setAdapter(adapter);

        return view;
    }


    void insertDataIngredients() {
        for (Ingredient ingredient : ingredientArrayList) {
            ContentValues foodValues = new ContentValues();
            foodValues.put(ItemsContract.IngredientEntry.COLUMNS_FOOD_NAME, food.getName().replace(" ", "_"));
            foodValues.put(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            foodValues.put(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredient());
            foodValues.put(ItemsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());

            getActivity().getContentResolver().insert(ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                    foodValues);
        }
    }

    //remove favourite from DB by using content provider
    void removeIngredients() {
        String newName = food.getName().replace(" ", "_");
        String[] selections = {newName};
        getActivity().getContentResolver().delete(
                ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                ItemsContract.IngredientEntry.COLUMNS_FOOD_NAME + " =? ",
                selections);
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public void setIngredientArrayList(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList("list", ingredientArrayList);
        currentState.putParcelable("step", step);
        if (mExoPlayer != null)
            currentState.putLong("videoPos", mExoPlayer.getCurrentPosition());
        currentState.putParcelable("food", food);
    }

    boolean isFavourite() {
        String newName = food.getName().replace(" ", "_");
        String[] selections = {newName};
        Cursor c = getActivity().getContentResolver().query(
                ItemsContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                null,
                ItemsContract.FoodEntry.COLUMN_FOOD_NAME + " =? ",
                selections,
                null);

        return c.getCount() > 0;

    }

    void correctFabImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isFavourite()) { //i set the fab icon based on that favs
                fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_border_24dp));
            } else {
                fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_full_24dp));
            }
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

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

        Log.d(TAG,"valye " + videoPosition);

    }

    private void initializePlayer(Uri mediaUri ) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            Log.d(TAG,"valye " + videoPosition);
            if (videoPosition > 0) {
                mExoPlayer.seekTo(videoPosition);
            }
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null)
            releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null)
            releasePlayer();
    }
}
