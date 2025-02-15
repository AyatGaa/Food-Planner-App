package com.example.foodplanner.favortitescreen.presenter;

import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.favortitescreen.view.FavoriteScreenView;
import com.example.foodplanner.utils.AppFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteScreenPresenterImpl implements FavoriteScreenPresenter{
 private FavoriteScreenView favoriteScreenView;
 private MealRepository mealRepository;



    public FavoriteScreenPresenterImpl(FavoriteScreenView favoriteScreenView, MealRepository mealRepository) {
        this.favoriteScreenView = favoriteScreenView;
        this.mealRepository = mealRepository;
    }

   @Override
    public void getFavoriteMeals() { //listen here for data from database!
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = AppFunctions.getCurrentUserId();
        if(user != null) {
            Log.d("fav", "Fetching meals for user: " + userId); // Debug log

            mealRepository.getAllFavouriteMeals(user.getUid()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(

                            meals -> {

                                for (Meal meal : meals) {
                                    Log.d("fav", "Meal: " + meal.getStrMeal() + ", ID: " + meal.getIdMeal() + ", UserID: " + meal.getUserId());
                                }
                                favoriteScreenView.showFavoriteMeals(meals);
                            },

                                    error -> Log.d("data", "Error getting favorite meals", error) // Error handling
                    );
        }else {

            Log.d("data", "User not signed in, can't fetch favorite meals.");
        }
    }

    @Override
    public void addMealToFavorite(Meal meal) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Meal meal1 = new Meal();
        meal1 = meal;
        if(user != null) {
            String userId = user.getUid();
            Log.d("fav", "Before setting user ID: " + meal1.getUserId());

            meal1.setUserId(userId);
                mealRepository.insertFavoriteMeal(meal1);

            Log.d("fav", "After setting user ID: " + meal1.getUserId());
            Log.d("fav", "Inserted meal: " + meal1.getStrMeal() + " for user: " + user);

            mealRepository.insertFavoriteMeal(meal1);
        }else {
            Log.d("fav", "User not authenticated. Cannot add meal to favorites.");

        }
      //  mealRepository.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteMealFromFavorite(Meal meal)
    {
        mealRepository.deleteFavouriteMeal(meal);
        favoriteScreenView.showSnackBar(meal);
    }
}
