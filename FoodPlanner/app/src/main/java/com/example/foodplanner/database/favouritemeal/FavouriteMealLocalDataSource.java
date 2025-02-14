package com.example.foodplanner.database.favouritemeal;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface FavouriteMealLocalDataSource  {

    Observable<List<Meal>> getAllFavouriteMeals();
    void insertFavoriteMeal(Meal meal);
    void deleteFavouriteMeal(Meal meal);
}
