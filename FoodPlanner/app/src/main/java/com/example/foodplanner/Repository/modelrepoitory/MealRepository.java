package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;
import com.example.foodplanner.network.callbacks.AreaCallback;
import com.example.foodplanner.network.callbacks.CategoryCallback;
import com.example.foodplanner.network.callbacks.IngredientNetworkcall;
import com.example.foodplanner.network.callbacks.MealDetailCallback;
import com.example.foodplanner.network.callbacks.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {

    //firbase fun
    void addMealToFirebase(Meal meal, String userId);
    Observable<List<Meal>> getFavouriteMealsFromFirebase(String userId);
    void deleteMealFromFirebase(Meal meal, String userId);

    public void fetchMealDetails(String mealId,  MealDetailCallback callback);

    Observable<List<Meal>> getAllFavouriteMeals(String userId);


    Observable<Meals> getMealsByCategory( NetworkCallback callBack,String category);
    Observable<Meals> getMealsByArea(NetworkCallback callBack,String area);
    Observable<Meals> getMealsByIngredient(NetworkCallback callBack,String ingredient);

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
