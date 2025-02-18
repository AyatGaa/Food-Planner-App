package com.example.foodplanner.backup;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface FavoriteMealFirebase {

    void addMealToFirebase(Meal meal, String userId);

    Observable<List<Meal>> getFavouriteMealsFromFirebase(String userId);

    void deleteMealFromFirebase(Meal meal, String userId);
}
