package com.ji.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ji.bakingapp.utils.Ingredient;

import java.util.ArrayList;

/**
 * Created by jonathanimperato on 08/03/18.
 */

public class UpdateWidgetService extends IntentService {

    public static String FROM_ACTIVITY_INGREDIENTS_LIST ="FROM_ACTIVITY_INGREDIENTS_LIST";

    public UpdateWidgetService() {
        super("UpdateBakingService");
    }

    public static void startBakingService(Context context, ArrayList<Ingredient> fromActivityIngredientsList) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Ingredient> fromActivityIngredientsList = intent.getExtras().getParcelableArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(fromActivityIngredientsList);
        }
    }



    private void handleActionUpdateBakingWidgets(ArrayList<Ingredient> fromActivityIngredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST,fromActivityIngredientsList);
        sendBroadcast(intent);
    }

}