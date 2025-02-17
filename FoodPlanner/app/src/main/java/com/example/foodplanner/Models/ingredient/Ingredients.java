package com.example.foodplanner.Models.ingredient;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ingredients {
    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
