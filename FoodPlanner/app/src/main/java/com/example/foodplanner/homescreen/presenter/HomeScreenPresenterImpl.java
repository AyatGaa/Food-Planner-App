package com.example.foodplanner.homescreen.presenter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealRepository;
import com.example.foodplanner.R;
import com.example.foodplanner.homescreen.view.HomeScreenView;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.network.RandomMealCallback;
import com.example.foodplanner.utils.AppFunctions;

import java.util.Collections;
import java.util.List;

public class HomeScreenPresenterImpl implements HomeScreenPresenter, NetworkCallback, RandomMealCallback {

    HomeScreenView homeScreenView;
    MealRepository mealRepository;

    Context context;

    public HomeScreenPresenterImpl(HomeScreenView homeScreenView, MealRepository mealRepository, Context context) {
        this.homeScreenView = homeScreenView;
        this.mealRepository = mealRepository;
        this.context = context;
    }

    @Override
    public void getMeals() {
        mealRepository.getAllMeals(this);
    }

    @Override
    public void getRandomMeal() {
        mealRepository.getRandomMeal(this);
    }


    @Override
    public void checkInternetConnection() {

        boolean isConnected = AppFunctions.isConnected(context);
        //     homeScreenView.setBottomNavEnabled(isConnected);
        if (!isConnected) {
            homeScreenView.showOnNoConnection();
        }
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
    public void onRandomMealSuccess(Meal meal) {
            homeScreenView.setRandmoMealCard(meal);
    }

    @Override
    public void onRandomMealFailure(String errorMessage) {
        Log.i("TAG", "onFailure: on  in Homescreen presenter" + errorMessage);
    }
}
