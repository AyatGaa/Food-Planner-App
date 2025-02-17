package com.example.foodplanner.database.plannedmeal;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface PlannedMealLocalDataSource {
    Observable<List<PlannedMeal>> getMealsForDate(String selectedDate);

    Observable<List<PlannedMeal>> getPlannedMealsByDate(String userId, String date);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    Observable<List<PlannedMeal>> getAllPlannedMeals();



    void deletePlannedMeal(PlannedMeal plannedMeal);
    void deletePastMeals(String currentDate);
}
