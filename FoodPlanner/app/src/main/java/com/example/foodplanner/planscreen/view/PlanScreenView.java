package com.example.foodplanner.planscreen.view;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

public interface PlanScreenView {
    void showPlannedMeals(List<PlannedMeal> meals);
}
