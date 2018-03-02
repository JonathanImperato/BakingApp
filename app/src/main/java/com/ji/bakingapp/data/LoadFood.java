package com.ji.bakingapp.data;

import android.content.Context;
import android.net.Uri;

import com.ji.bakingapp.utils.Food;
import com.ji.bakingapp.utils.Ingredient;
import com.ji.bakingapp.utils.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class LoadFood extends android.support.v4.content.AsyncTaskLoader<Food[]> {

    public LoadFood(Context context) {
        super(context);
    }


    @Override
    public Food[] loadInBackground() {
        return getFoodsList();
    }

    @Override
    public void deliverResult(Food[] data) {
        super.deliverResult(data);
    }


    private java.net.URL buildUrl() {
        String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        Uri moviesUri = Uri.parse(BASE_URL).buildUpon()
                .build();

        try {
            URL movieUrl = new URL(moviesUri.toString());
            return movieUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Food[] getFoodsList() {
        try {
            URL builtUrl = buildUrl();
            String jsonString = getHttpResponseFromUrl(builtUrl);
            Food[] foodList = getFoods(jsonString);
            return foodList;

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
            return null;
        }
    }


    public Food[] getFoods(String jsonString) {
        JSONArray root = null;
        try {
            root = new JSONArray(jsonString);

            Food[] foodlist = new Food[root.length()];

            for (int i = 0; i < root.length(); i++) {
                JSONObject item = root.getJSONObject(i);
                Food food = new Food();
                if (item.has("name")) {
                    String name = item.optString("name");
                    food.setName(name);
                }
                if (item.has("servings")) {
                    int servings = item.optInt("servings");
                    food.setServings(servings);
                }
                if (item.has("image")) {
                    String image = item.optString("image");
                    if (image.length() == 0) food.setImage(" ");
                    food.setImage(image);
                }
                if (item.has("ingredients")) {
                    JSONArray ingredientsArray = item.getJSONArray("ingredients");
                    ArrayList<Ingredient> ingredientList = new ArrayList<>();
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        Ingredient ingredient = new Ingredient(
                                ingredientsArray.getJSONObject(j).getInt("quantity"),
                                ingredientsArray.getJSONObject(j).getString("measure"),
                                ingredientsArray.getJSONObject(j).getString("ingredient")
                        );
                        ingredientList.add(ingredient);
                    }
                    food.setIngredients(ingredientList);

                }
                if (item.has("steps")) {
                    JSONArray stepsArray = item.getJSONArray("steps");
                    ArrayList<Step> stepList = new ArrayList<>();
                    for (int j = 0; j < stepsArray.length(); j++) {
                        Step step = new Step(
                                stepsArray.getJSONObject(j).getInt("id"),
                                stepsArray.getJSONObject(j).getString("shortDescription"),
                                stepsArray.getJSONObject(j).getString("description"),
                                stepsArray.getJSONObject(j).getString("videoURL"),
                                stepsArray.getJSONObject(j).getString("thumbnailURL")
                        );
                        stepList.add(step);
                    }
                    food.setSteps(stepList);
                }
                foodlist[i] = (food);
            }
            return foodlist;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getHttpResponseFromUrl(URL url) {
        HttpsURLConnection con = null;
        try {
            URL u = url;
            con = (HttpsURLConnection) u.openConnection();
            con.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return null;
    }
}