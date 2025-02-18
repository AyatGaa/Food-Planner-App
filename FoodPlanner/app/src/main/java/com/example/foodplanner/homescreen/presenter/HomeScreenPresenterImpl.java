package com.example.foodplanner.homescreen.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealOnlyRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
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
    MealOnlyRepository mealOnlyRepository;
    MealRepository mealRepository;

    Context context;

    public HomeScreenPresenterImpl(HomeScreenView homeScreenView, MealOnlyRepository mealOnlyRepository, Context context ,  MealRepository mrepo) {
        this.homeScreenView = homeScreenView;
        this.mealOnlyRepository = mealOnlyRepository;
        this.context = context;
        this.mealRepository = mrepo;
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
        //     homeScreenView.setBottomNavEnabled(isConnected);
        if (!isConnected) {
            homeScreenView.showOnNoConnection();
        }
    }

    @Override
    public void getFavoriteMealsFirebase() {
        String userId = AppFunctions.getCurrentUserId();
        Log.d("fb", "Fetching meals for user: " + userId); // Log userId for debugging

        mealRepository.getFavouriteMealsFromFirebase(userId) // ✅ Now it returns Observable<List<Meal>>
                .subscribeOn(Schedulers.io()) // ✅ Perform operation on background thread
                .observeOn(AndroidSchedulers.mainThread()) // ✅ Observe results on UI thread
                .subscribe(
                        meals -> {Toast.makeText(context, "add to fir base and room", Toast.LENGTH_SHORT).show();

                           for (Meal meal : meals) {

                            mealRepository.insertFavoriteMeal(meal);
                           }
                            }, // ✅ Success: Update UI
                        error -> {
                            Log.e("fb", "Error fetching favorite meals: " + error.getMessage()); // ✅ Handle errors

                        }
                );
    }



    @Override
    public void onSuccess(List<Meal> meals) {

        homeScreenView.showMeals(meals);
        Log.i("TAG", "onSuccess: in Homescreen presenter" + meals.size());
    }

    @Override
    public void onSuccessArea(List<Meal> meals) {

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
