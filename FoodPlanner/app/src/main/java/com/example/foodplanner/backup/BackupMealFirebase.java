package com.example.foodplanner.backup;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface BackupMealFirebase {

    void addMealToFirebase(Meal meal, String userId);

    Observable<List<Meal>> getFavouriteMealsFromFirebase(String userId);

    void deleteMealFromFirebase(Meal meal, String userId);


    //planned meal
    void addPlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate);

    Observable<List<PlannedMeal>> getPlannedMealsFromFirebase(String userId, String plannedDate);
    void deletePlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate);
    Observable<List<PlannedMeal>> getPlannedMealsFromFirebaseByDate(String userId, String plannedDate);

}
