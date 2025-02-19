package com.example.foodplanner.network.datasources;

import com.example.foodplanner.Models.meals.Meals;
import com.example.foodplanner.network.callbacks.NetworkCallback;
import com.example.foodplanner.network.callbacks.RandomMealCallback;

import io.reactivex.rxjava3.core.Single;

public interface MealRemoteDataSource {
    void mealNetworkCall(NetworkCallback callBack);
    void randomMealNetworkCall(RandomMealCallback callBack);
    Single<Meals> getAllMeals(NetworkCallback callBack, String mealName);




    void  filterMealByArea(NetworkCallback callBack, String area);
    void filterMealByIngredient(NetworkCallback callBack, String ingredient);
    void searchMealByName(NetworkCallback callBack, String mealName);
    void getAllAreas(NetworkCallback callBack);

}
