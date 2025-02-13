package com.example.foodplanner.homescreen.view;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface HomeScreenView {
    void showOnNoConnection();
    void clearUI();
    void setNoConnectionUI();
    void setBottomNavEnabled(boolean isConnected);

    void showMeals(List<Meal> meals);
   void setRandmoMealCard(Meal meals);
}
