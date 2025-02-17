package com.example.foodplanner.network;

import com.example.foodplanner.Models.meals.Meal;

public interface MealDetailCallback {
    void onMealDetailsFetched(Meal meal); // For success
    void onFailure(String message);
}
