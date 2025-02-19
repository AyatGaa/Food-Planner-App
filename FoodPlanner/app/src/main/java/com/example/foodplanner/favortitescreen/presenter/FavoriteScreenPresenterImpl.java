package com.example.foodplanner.favortitescreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.favortitescreen.view.FavoriteScreenView;
import com.example.foodplanner.utils.AppFunctions;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteScreenPresenterImpl implements FavoriteScreenPresenter {
    private FavoriteScreenView favoriteScreenView;
    private MealRepository mealRepository;

    public FavoriteScreenPresenterImpl(FavoriteScreenView favoriteScreenView, MealRepository mealRepository) {
        this.favoriteScreenView = favoriteScreenView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void getFavoriteMeals() { //listen here for data from database!
        String userId = AppFunctions.getCurrentUserId();
        if (userId != null) {

            mealRepository.getAllFavouriteMeals(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            meals -> {
                                favoriteScreenView.showFavoriteMeals(meals);
                            },
                            error -> Log.d("data", "Error getting favorite meals", error) // Error handling
                    );
        } else {
            Log.d("data", "User not signed in, can't fetch favorite meals.");
        }
    }

    @Override
    public void addMealToFavorite(Meal meal) {
        String user = AppFunctions.getCurrentUserId();
        if (user != null) {
            meal.setUserId(user);//must be set before inserting
            mealRepository.insertFavoriteMeal(meal);
        } else {
            Log.d("fav", "User not authenticated. Cannot add meal to favorites.");
        }
    }

    @Override
    public void deleteMealFromFavorite(Meal meal) {
        mealRepository.deleteFavouriteMeal(meal);
        favoriteScreenView.showSnackBar(meal, "Meal deleted");
    }

    @Override
    public void getFavouriteMealsFromFirebase(String userId) {
            mealRepository.getFavouriteMealsFromFirebase(userId);
    }

    @Override
    public void checkInternetConnection(Context context) {
        boolean isConnected = AppFunctions.isConnected(context);
        if (!isConnected) {
           getFavoriteMeals();
        }
    }
}
