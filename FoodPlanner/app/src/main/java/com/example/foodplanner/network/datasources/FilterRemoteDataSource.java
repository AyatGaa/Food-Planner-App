package com.example.foodplanner.network.datasources;

import com.example.foodplanner.Models.meals.Meals;
import com.example.foodplanner.network.callbacks.AreaCallback;
import com.example.foodplanner.network.callbacks.CategoryCallback;
import com.example.foodplanner.network.callbacks.IngredientNetworkcall;
import com.example.foodplanner.network.callbacks.NetworkCallback;

import io.reactivex.rxjava3.core.Observable;

public interface FilterRemoteDataSource {

    void categoryNetworkCall(CategoryCallback callBack);
    void areaNetworkCall(AreaCallback callBack);
    void ingredientNetworkCall(IngredientNetworkcall callBack);
    void filterMealByCategory(NetworkCallback callBack, String category);
    void filterMealByArea(NetworkCallback callBack, String area);
    void filterMealByIngredient(NetworkCallback callBack, String ingredient);

    public Observable<Meals> getMealById(String mealId);
   Observable<Meals> getMealsByCategory(NetworkCallback callBack,String category);
    Observable<Meals>  getMealsByArea(NetworkCallback callBack,String area);
    Observable<Meals> getMealsByIngredient(NetworkCallback callBack,String ingredient);

}
