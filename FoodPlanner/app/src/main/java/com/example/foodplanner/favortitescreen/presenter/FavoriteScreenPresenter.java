package com.example.foodplanner.favortitescreen.presenter;

import android.content.Context;

import com.example.foodplanner.Models.meals.Meal;

public interface FavoriteScreenPresenter {

    void getFavoriteMeals();
    void addMealToFavorite(Meal meal);
    void deleteMealFromFavorite(Meal meal);
    void getFavouriteMealsFromFirebase(String userId);

    public void checkInternetConnection(Context context) ;
}
