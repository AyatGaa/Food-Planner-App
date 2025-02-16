package com.example.foodplanner.network;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface MealRemoteDataSource {
    void mealNetworkCall(NetworkCallback callBack);
    void randomMealNetworkCall(RandomMealCallback callBack);

    void  filterMealByArea(NetworkCallback callBack, String area);
    Single<Meals> getAllMeals(NetworkCallback callBack, String mealName);
    void filterMealByIngredient(NetworkCallback callBack, String ingredient);
    void searchMealByName(NetworkCallback callBack, String mealName);
    void getAllAreas(NetworkCallback callBack);

}
