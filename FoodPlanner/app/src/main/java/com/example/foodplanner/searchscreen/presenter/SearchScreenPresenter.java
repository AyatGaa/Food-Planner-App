package com.example.foodplanner.searchscreen.presenter;

import com.example.foodplanner.network.AreaCallback;
import com.example.foodplanner.network.CategoryCallback;
import com.example.foodplanner.network.NetworkCallback;

public interface SearchScreenPresenter {

    void checkInternetConnection();
    void filterByCategory(String category);
    void filterByArea(String area);
    void filterByIngredient(String ingredient);

    void getAllCategories();
    void getAllAreas();
    void getAllIngredients();
    void mealNetworkCall();
    void getAllMeals(String meal);


    void getMealsByCategory(String category);
    void getMealsByArea(String area);
    void getMealsByIngredient(String ingredient);


}
