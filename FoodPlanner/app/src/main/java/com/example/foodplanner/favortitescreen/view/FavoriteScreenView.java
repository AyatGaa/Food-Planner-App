package com.example.foodplanner.favortitescreen.view;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface FavoriteScreenView {
   void showFavoriteMeals(List<Meal> meals);
   void showSnackBar(Meal meal , String message);
}
