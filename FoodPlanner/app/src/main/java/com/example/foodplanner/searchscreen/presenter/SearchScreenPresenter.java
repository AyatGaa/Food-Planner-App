package com.example.foodplanner.searchscreen.presenter;

import com.example.foodplanner.network.AreaCallback;
import com.example.foodplanner.network.CategoryCallback;
import com.example.foodplanner.network.NetworkCallback;

public interface SearchScreenPresenter {

    void checkInternetConnection();

    void getAllMeals(String meal);
    //category
    void getAllCategories();
    void getMealsByCategory(String category);
    //ingredient
    void getAllIngredients();
    void getMealsByIngredient(String ingredient);
    //area
    void getAllAreas();
    void getMealsByArea(String area);


    //for further use
    void mealNetworkCall();

    void filterByCategory(String category);

    void filterByArea(String area);

    void fetchMealDetails(String mealId);

    void filterByIngredient(String ingredient);

}
