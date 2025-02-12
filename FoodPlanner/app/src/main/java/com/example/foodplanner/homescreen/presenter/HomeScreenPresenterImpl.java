package com.example.foodplanner.homescreen.presenter;

import android.util.Log;

import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealRepository;
import com.example.foodplanner.homescreen.view.HomeScreenView;
import com.example.foodplanner.network.NetworkCallback;

import java.util.Collections;
import java.util.List;

public class HomeScreenPresenterImpl implements HomeScreenPresenter , NetworkCallback {

    HomeScreenView homeScreenView;
    MealRepository mealRepository;

    public HomeScreenPresenterImpl(HomeScreenView homeScreenView, MealRepository mealRepository) {
        this.homeScreenView = homeScreenView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void getMeals() {
        mealRepository.getAllMeals(this);
    }

    @Override
    public void addMealToFavourite(Meal meal) {

    }

    @Override
    public void onSuccess(List<Meal> meals) {
        homeScreenView.showMeals(meals);
        Log.i("TAG", "onSuccess: in Homescreen presenter" + meals.size());
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.i("TAG", "onFailure: on  in Homescreen presenter" + errorMessage);
    }

    @Override
    public void onCategorySuccess(List<Category> categories) {

    }

    @Override
    public void onCategoryFailure(String errorMessage) {

    }
}
