package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface PlanRepository {
    Observable<List<PlannedMeal>> getMealsForDate(String selectedDate);
    Observable<List<Meal>> getAllPlannedMeals();
    void insertPlannedMeal(PlannedMeal plannedMeal);
    void deletePlannedMeal(PlannedMeal plannedMeal);
    void deletePastMeals(String currentDate);

}
