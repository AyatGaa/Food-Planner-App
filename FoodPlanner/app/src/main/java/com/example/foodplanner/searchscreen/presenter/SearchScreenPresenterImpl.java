package com.example.foodplanner.searchscreen.presenter;

import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.searchscreen.view.SearchScreenView;

import java.util.List;

import javax.security.auth.login.LoginException;

public class SearchScreenPresenterImpl implements SearchScreenPresenter {

    MealRepository repo;
    SearchScreenView view;

    public SearchScreenPresenterImpl(MealRepository repo, SearchScreenView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void filterByCategory(String selectedCategory) {
            repo.filterByCategory(new NetworkCallback() {
                @Override
                public void onSuccess(List<Meal> meals) {
                    Log.i("cat", "onSuccess:category  =>" + meals.get(0));
                    view.showListOfMealByCategorty(meals);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.d("cat", "onFailure: " +errorMessage);
                }

                @Override
                public void onRandomMealSuccess(Meal meal) {

                }

                @Override
                public void onRandomMealFailure(String errorMessage) {

                }
            }, selectedCategory);
    }

    @Override
    public void filterByArea(String area) {
        repo.filterByArea(new NetworkCallback() {


            @Override
            public void onSuccess(List<Meal> meals) {
                view.showListOfMealByArea(meals);
                Log.i("cat", "onSuccess:area  =>" + meals.get(0));
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i("cat", "onFailure: area "+errorMessage);
            }

            @Override
            public void onRandomMealSuccess(Meal meal) {

            }

            @Override
            public void onRandomMealFailure(String errorMessage) {

            }
        },area);
    }

    @Override
    public void filterByIngredient(String ingredient) {
        repo.filterByIngredient(new NetworkCallback() {

            @Override
            public void onSuccess(List<Meal> meals) {
                view.showListOfMealByIngredient(meals);
                Log.i("cat", "onSuccess: ingredient");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i("cat", "onFailure: ingred");
            }

            @Override
            public void onRandomMealSuccess(Meal meal) {

            }

            @Override
            public void onRandomMealFailure(String errorMessage) {

            }
        } ,ingredient);
    }


}
