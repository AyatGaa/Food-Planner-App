package com.example.foodplanner.network.callbacks;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface NetworkCallback {

    void onSuccess(List<Meal> meals);

    void onSuccessArea(List<Meal> meals);
    void onFailure(String errorMessage);

}
