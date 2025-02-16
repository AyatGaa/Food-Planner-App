package com.example.foodplanner.searchscreen.presenter;

import com.example.foodplanner.network.NetworkCallback;

public interface SearchScreenPresenter {


    void filterByCategory(String category);
    void filterByArea(String area);
    void filterByIngredient(String ingredient);
}
