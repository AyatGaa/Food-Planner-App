package com.example.foodplanner.detailedmeal.presenter;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

public interface DetailedMealPresenter {
      void fetchMealDetails(String mealId);
    void onAddToFavourite(Meal meal);

    void onAddToPlan(PlannedMeal planedMeal);
   boolean isFutureDate(String selectedDate);
    String extractYoutubeVideoId(String url);
    List<String> getIngredient(Meal meal);



}
