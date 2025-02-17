package com.example.foodplanner.network;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import io.reactivex.rxjava3.core.Observable;

public interface FilterRemoteDataSource {

    void categoryNetworkCall(CategoryCallback callBack);
    void areaNetworkCall(AreaCallback callBack);
    void ingredientNetworkCall(IngredientNetworkcall callBack);
    void filterMealByCategory(NetworkCallback callBack, String category);
    void filterMealByArea(NetworkCallback callBack, String area);
    void filterMealByIngredient(NetworkCallback callBack, String ingredient);


   Observable<Meals> getMealsByCategory(NetworkCallback callBack,String category);
    Observable<Meals>  getMealsByArea(NetworkCallback callBack,String area);
    Observable<Meals> getMealsByIngredient(NetworkCallback callBack,String ingredient);

}
