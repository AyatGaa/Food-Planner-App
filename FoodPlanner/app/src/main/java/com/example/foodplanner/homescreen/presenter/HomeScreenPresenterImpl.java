package com.example.foodplanner.homescreen.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealOnlyRepository;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.favortitescreen.view.FavoriteScreenView;
import com.example.foodplanner.homescreen.view.HomeScreenView;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.network.RandomMealCallback;
import com.example.foodplanner.utils.AppFunctions;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeScreenPresenterImpl implements HomeScreenPresenter, NetworkCallback, RandomMealCallback {
    HomeScreenView homeScreenView;
    MealOnlyRepository mealOnlyRepository; //for API meal calls in home screen Only
    MealRepository mealRepository;
    PlanRepository planRepository;
    Context context;
    String TAG = "MealOnlyRepo";

    public HomeScreenPresenterImpl(HomeScreenView homeScreenView, MealOnlyRepository mealOnlyRepository,
                                   Context context, MealRepository mealRepository,
                                   PlanRepository planRepository) {
        this.homeScreenView = homeScreenView;
        this.mealOnlyRepository = mealOnlyRepository;
        this.context = context;
        this.mealRepository = mealRepository;
        this.planRepository = planRepository;
    }

    @Override
    public void getMeals() {
        mealOnlyRepository.getAllMeals(this);
    }

    @Override
    public void getRandomMeal() {
        mealOnlyRepository.getRandomMeal(this);
    }
    
    @Override
    public void checkInternetConnection() {
        boolean isConnected = AppFunctions.isConnected(context);
        if (!isConnected) {
            homeScreenView.showOnNoConnection();
        }
    }


    @SuppressLint("CheckResult")
    @Override
    public void getFavoriteMealsFirebase() {
        if(AppFunctions.isAuthenticated() ){
        String userId = AppFunctions.getCurrentUserId();

        mealRepository.getFavouriteMealsFromFirebase(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            for (Meal meal : meals) {// to add meals from firebase to room
                                mealRepository.insertFavoriteMeal(meal);
                            }
                        },
                        error -> {
                            Log.e("fb", "Error fetching favorite meals: " + error.getMessage());
                        }
                );
        }else{
            Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("CheckResult")
    @Override
    public void getPlannedMealsFirebase(String plannedDate) {
        if(AppFunctions.isAuthenticated() ) {
            String userId = AppFunctions.getCurrentUserId();
            planRepository.getPlannedMealsFromFirebase(userId, plannedDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            meals -> {
                                for (PlannedMeal meal : meals) {// to add meals from firebase to room
                                    planRepository.insertPlannedMeal(meal);
                                }
                            },
                            error -> {
                                Log.e("fb", "Error fetching PLanned meals: " + error.getMessage());
                            }
                    );
        }else{
            Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSuccess(List<Meal> meals) { //call of MealOnlyRepo -_-
        homeScreenView.showMeals(meals);
    }
    @Override
    public void onRandomMealSuccess(Meal meal) {
        homeScreenView.setRandmoMealCard(meal);
    }

    @Override
    public void onRandomMealFailure(String errorMessage) {
    }

    @Override
    public void onSuccessArea(List<Meal> meals) {
        Log.i(TAG, "onSuccessArea: ");
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.e(TAG, "onFailure: "+errorMessage  );
    }
    
}
