package com.example.foodplanner.network;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Query;

public interface MealRemoteDataSource {
    void mealNetworkCall(NetworkCallback callBack);
    void randomMealNetworkCall(RandomMealCallback callBack);
    Single<Meals> getAllMeals(NetworkCallback callBack, String mealName);




    void  filterMealByArea(NetworkCallback callBack, String area);
    void filterMealByIngredient(NetworkCallback callBack, String ingredient);
    void searchMealByName(NetworkCallback callBack, String mealName);
    void getAllAreas(NetworkCallback callBack);

}
