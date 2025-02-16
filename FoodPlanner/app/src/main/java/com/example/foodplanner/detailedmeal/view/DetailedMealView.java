package com.example.foodplanner.detailedmeal.view;

import com.example.foodplanner.Models.meals.Meal;

public interface DetailedMealView {

    void showAddedSnackBar(Meal meal, String message);

    void showVideoPlayer(Meal meal);

    void showDatePickers(Meal meal);
}
