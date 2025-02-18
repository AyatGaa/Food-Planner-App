package com.example.foodplanner.database.plannedmeal;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlanMealDao {


    @Query("SELECT * FROM planned_meals WHERE idMeal = :mealId LIMIT 1")
    Observable<PlannedMeal> getPlannedMealById(String mealId);
    @Query("SELECT * FROM planned_meals WHERE date = :selectedDate")
    Observable<List<PlannedMeal>> getMealsForDate(String selectedDate);
    @Query("SELECT * FROM planned_meals WHERE userId = :userId AND date = :date")
    Observable<List<PlannedMeal>> getPlannedMealsByDate(String userId, String date);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlannedMeal(PlannedMeal plannedMeal);
    @Query("SELECT * FROM planned_meals")
    Observable<List<PlannedMeal>> getAllPlannedMeals();

    @Delete
    Completable deletePlannedMeal(PlannedMeal plannedMeal);

    @Query("DELETE FROM planned_meals WHERE date < :currentDate")
    Completable deletePastMeals(String currentDate);

    @Query("SELECT COUNT(*) FROM planned_meals WHERE idMeal = :idMeal AND date = :selectedDate")
    Single<Integer> isMealPlanned(String idMeal, String selectedDate);

}
