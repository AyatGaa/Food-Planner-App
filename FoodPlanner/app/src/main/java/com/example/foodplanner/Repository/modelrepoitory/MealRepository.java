package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;
import com.example.foodplanner.network.AreaCallback;
import com.example.foodplanner.network.CategoryCallback;
import com.example.foodplanner.network.IngredientNetworkcall;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {

    Observable<List<Meal>> getAllFavouriteMeals(String userId);
    void insertFavoriteMeal(Meal meal);
    void deleteFavouriteMeal(Meal meal);

    void filterByCategory(NetworkCallback callBack, String category);
    void filterByArea(NetworkCallback callBack, String area);
    void filterByIngredient(NetworkCallback callBack, String ingredient);


    void getAllCategories(CategoryCallback callBack);
    void getAllAreas(AreaCallback callBack);
    void getAllIngredients(IngredientNetworkcall callBack);

    Single<Meals> searchMealByName(NetworkCallback callBack, String mealName);

    void mealNetworkCall(NetworkCallback callBack);
}
