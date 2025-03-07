package com.example.foodplanner.homescreen.presenter;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface HomeScreenPresenter {

    void getMeals();

    void getRandomMeal();

    void checkInternetConnection();

    void getFavoriteMealsFirebase();

    void getPlannedMealsFirebase(String plannedDate);
}
