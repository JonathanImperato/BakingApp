package com.ji.bakingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jonathanimperato on 01/03/18.
 */

public class Food implements Parcelable {
    String name;
    int servings;
    int id;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;

    String image;

    public Food() {
    }

    public Food(String name, int servings, int id, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, String image) {
        this.name = name;
        this.servings = servings;
        this.id = id;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image = image;
    }

    protected Food(Parcel in) {
        name = in.readString();
        servings = in.readInt();
        id = in.readInt();
        image = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public String getImage() {
        if (image.isEmpty() || image == null) return null;
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeInt(id);
        parcel.writeString(image);
    }
}
