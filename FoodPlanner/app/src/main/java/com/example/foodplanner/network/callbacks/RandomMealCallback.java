package com.example.foodplanner.network.callbacks;

import com.example.foodplanner.Models.meals.Meal;

public interface RandomMealCallback {
    void onRandomMealSuccess(Meal meal);
    void onRandomMealFailure(String errorMessage);
}
