package com.example.foodplanner.favortitescreen.presenter;

import com.example.foodplanner.Models.meals.Meal;

public interface FavoriteScreenPresenter {

    void getFavoriteMeals();
    void addMealToFavorite(Meal meal);
    void deleteMealFromFavorite(Meal meal);
}
