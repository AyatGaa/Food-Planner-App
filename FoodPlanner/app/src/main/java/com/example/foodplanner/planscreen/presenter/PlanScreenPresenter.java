package com.example.foodplanner.planscreen.presenter;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

public interface PlanScreenPresenter {


    void getMealsForDate(String selectedDate);
    void deleteMealFromPlan(PlannedMeal meal);

    public void getPlannedMealsFirebase( String plannedDate);

    void getAllPlannedMeals();

    void addMealToPlan(PlannedMeal plannedMeal);

    void addMealToPlanV2(Meal meal, String date);
}
