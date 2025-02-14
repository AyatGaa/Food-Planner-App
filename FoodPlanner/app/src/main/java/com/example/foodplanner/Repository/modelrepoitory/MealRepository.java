package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MealRepository {

    Observable<List<Meal>> getAllFavouriteMeals();
    void insertFavoriteMeal(Meal meal);
    void deleteFavouriteMeal(Meal meal);

    void mealNetworkCall(NetworkCallback callBack);
}
