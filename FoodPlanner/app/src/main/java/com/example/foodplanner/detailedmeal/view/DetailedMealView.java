package com.example.foodplanner.detailedmeal.view;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface DetailedMealView {

    void showAddedSnackBar(Meal meal, String message);

    void showVideoPlayer(Meal meal);

    void showDatePickers(Meal meal);
    void showMealList(List<Meal> meals);
    void showMealDetails(Meal meal);
}
