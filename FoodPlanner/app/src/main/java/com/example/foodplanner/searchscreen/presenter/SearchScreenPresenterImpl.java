package com.example.foodplanner.searchscreen.presenter;

import android.util.Log;
import android.view.View;

import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.network.AreaCallback;
import com.example.foodplanner.network.CategoryCallback;
import com.example.foodplanner.network.IngredientNetworkcall;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.searchscreen.view.SearchScreenView;

import java.util.List;

import javax.security.auth.login.LoginException;

import io.reactivex.rxjava3.disposables.Disposable;

public class SearchScreenPresenterImpl implements SearchScreenPresenter, IngredientNetworkcall, CategoryCallback, AreaCallback{

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

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        }, selectedCategory);
    }

    @Override
    public void filterByArea(String area) {
        repo.filterByArea(new NetworkCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }


        }, area);
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

        }, ingredient);
    }

    @Override
    public void getAllCategories() {
        repo.getAllCategories(this);
    }

//    @Override
//    public void getAllAreas() {
//        repo.getAllAreas(this);
//    }

    @Override
    public void getAllAreas() {
        Log.d("PRESENTER", "getAllAreas: Calling repository to fetch areas.");
        repo.getAllAreas(this);
    }

    @Override
    public void getAllIngredients() {
        repo.getAllIngredients(this);
    }

    @Override
    public void mealNetworkCall() {

    }

    @Override
    public void onAreaSuccess(List<Area> areas) {
        view.showAllAreas(areas);
    }

    @Override
    public void onAreaFailure(String errorMessage) {
        Log.d("TAG", "onAreaFailure: ");
    }

    @Override
    public void onCategorySuccess(List<Category> categories) {
        view.showAllCategories(categories);

    }

    @Override
    public void onCategoryFailure(String errorMessage) {
        Log.d("TAG", "onCategoryFailure: ");
    }

    @Override
    public void onIngredientSuccess(List<Ingredient> ingredientList) {
            view.showAllIngredients(ingredientList);
    }

    @Override
    public void onIngredientFailure(String errorMsg) {
        Log.d("TAG", "onIngredientFailure: ");
    }
}
