package com.ji.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.ji.bakingapp.MainActivity;
import com.ji.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews remoteViews = getRecipeGridRemoteView(context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static RemoteViews getRecipeGridRemoteView(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        Intent gridIntent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.grid, gridIntent);
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent wateringPendingIntent = PendingIntent.getService(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

   //     views.setOnClickPendingIntent(R.id.grid, wateringPendingIntent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null)
            if ("android.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appwidgetIds = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, getClass())
                );
                appWidgetManager.notifyAppWidgetViewDataChanged(appwidgetIds, R.id.grid);
                onUpdate(context, appWidgetManager, appwidgetIds);
            }
    }


}

