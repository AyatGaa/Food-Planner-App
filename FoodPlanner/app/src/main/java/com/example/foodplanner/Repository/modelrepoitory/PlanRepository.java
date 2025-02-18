package com.example.foodplanner.Repository.modelrepoitory;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface PlanRepository {


    Observable<PlannedMeal> getPlannedMealById(String mealId);
    //firebase
//planned meal
    void addPlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate);

    Observable<List<PlannedMeal>> getPlannedMealsFromFirebase(String userId, String plannedDate);

    void deletePlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate);

//Room
    Observable<List<PlannedMeal>> getPlannedMealsByDate(String userId, String date);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    Observable<List<PlannedMeal>> getAllPlannedMeals();

    void deletePlannedMeal(PlannedMeal plannedMeal);

    //NOT USED
    Observable<List<PlannedMeal>> getMealsForDate(String selectedDate);

    void deletePastMeals(String currentDate);

}
