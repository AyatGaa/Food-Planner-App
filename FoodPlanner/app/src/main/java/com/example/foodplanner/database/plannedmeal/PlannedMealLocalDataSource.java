package com.example.foodplanner.database.plannedmeal;

import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface PlannedMealLocalDataSource {
    void insertPlannedMeal(PlannedMeal plannedMeal);
    void deletePlannedMeal(PlannedMeal plannedMeal);
    Observable<PlannedMeal> getPlannedMealById(String mealId);

    Observable<List<PlannedMeal>> getPlannedMealsByDate(String userId, String date);

    Observable<List<PlannedMeal>> getAllPlannedMeals();


    void deletePastMeals(String currentDate);
    Observable<List<PlannedMeal>> getMealsForDate(String selectedDate);

}
