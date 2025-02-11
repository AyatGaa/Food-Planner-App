package com.example.foodplanner.network;

import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface NetworkCallback {

    void onSuccess(List<Meal> meals);

    void onFailure(String errorMessage);

    void onCategorySuccess(List<Category> categories);
    void onCategoryFailure(String errorMessage);

}
