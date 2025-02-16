package com.example.foodplanner.Models.ingredient;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("strIngredient")
    private String strIngredient;

    public String getStrIngredient() {
        return strIngredient;
    }
}
