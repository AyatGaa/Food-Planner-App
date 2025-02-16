package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MealRepository {

    Observable<List<Meal>> getAllFavouriteMeals(String userId);
    void insertFavoriteMeal(Meal meal);
    void deleteFavouriteMeal(Meal meal);

    void filterByCategory(NetworkCallback callBack, String category);
    void filterByArea(NetworkCallback callBack, String area);
    void filterByIngredient(NetworkCallback callBack, String ingredient);

    void mealNetworkCall(NetworkCallback callBack);
}
