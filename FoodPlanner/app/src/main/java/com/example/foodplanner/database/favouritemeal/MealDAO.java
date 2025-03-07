package com.example.foodplanner.database.favouritemeal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDAO { //for meals and favourite meals

    @Query("SELECT * FROM meal WHERE userId = :userId")
    Observable<List<Meal>> getAllFavouriteMeals(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavoriteMeal(Meal meal);
    @Delete
    Completable deleteFavouriteMeal(Meal meal);

    @Query("SELECT * FROM meal WHERE idMeal = :idMeal")
    Single<Meal> getMealById(String idMeal);

}
