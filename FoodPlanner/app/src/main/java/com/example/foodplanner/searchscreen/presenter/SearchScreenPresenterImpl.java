package com.example.foodplanner.searchscreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.network.callbacks.AreaCallback;
import com.example.foodplanner.network.callbacks.CategoryCallback;
import com.example.foodplanner.network.callbacks.IngredientNetworkcall;
import com.example.foodplanner.network.callbacks.MealDetailCallback;
import com.example.foodplanner.network.callbacks.NetworkCallback;
import com.example.foodplanner.searchscreen.view.SearchScreenView;
import com.example.foodplanner.utils.AppFunctions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchScreenPresenterImpl implements SearchScreenPresenter, IngredientNetworkcall, CategoryCallback, AreaCallback, NetworkCallback {

    MealRepository repo;
    SearchScreenView view;
    Context context;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SearchScreenPresenterImpl(MealRepository repo, SearchScreenView view, Context context) {
        this.repo = repo;
        this.view = view;
        this.context = context;
    }

    @Override
    public void checkInternetConnection() {
        boolean isConnected = AppFunctions.isConnected(context);
        if (!isConnected) {
            view.showOnNoConnectionSearch();
        }
    }


    @Override
    public void getAllCategories() {
        repo.getAllCategories(this);
    }

    @Override
    public void getAllAreas() {
        Log.d("area", "getAllAreas: Calling repository to fetch areas.");
        repo.getAllAreas(this);
    }

    @Override
    public void getAllIngredients() {repo.getAllIngredients(this);
    }

    @Override
    public void mealNetworkCall() {

    }

    @Override
    public void getAllMeals(String meal) {
        disposable.add(repo.searchMealByName(this, meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> view.showMealList(mealResponse.getMeals()),
                        error -> Log.d("TAG", "getAllMeals: error in search  " + error.getMessage())
                ));
        repo.searchMealByName(this, meal);
    }

    @Override
    public void getMealsByCategory(String category) {
        repo.getMealsByCategory(this, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        mealResponse -> {
                            view.showMealList(mealResponse.getMeals());
                        },
                        throwable -> Log.e("SearchPresenter", "Error fetching meals by category", throwable)
                );
    }

    @Override
    public void getMealsByArea(String area) {
        List<Meal> all = new ArrayList<>();
        disposable.add(repo.getMealsByArea(this, area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> {
                            if (mealResponse != null && mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                                for (Meal m : mealResponse.getMeals()) {
                                    all.add(m);
                                }
                                view.showMealList(all);
                            } else {
                                Log.w("Presenter", "No meals found for area: " + area);
                            }
                        },
                        throwable -> {
                            Log.e("Presenter", "Error fetching meals by area", throwable);
                        }
                ));
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        repo.getMealsByIngredient(this, ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> view.showMealList(mealResponse.getMeals()),
                        throwable -> Log.e("SearchPresenter", "Error fetching meals by ingredient", throwable)
                );


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

    @Override
    public void onSuccess(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            view.showMealList(meals);
        } else {
            Log.w("Chip", "No meals found.");
        }
    }

    @Override
    public void onSuccessArea(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            view.showMealList(meals);
        } else {
            Log.w("Chip", "No meals found.");
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.d("TAG", "meals: ");
    }


    public void fetchMealDetails(String mealId) {
        repo.fetchMealDetails(mealId, new MealDetailCallback() {
            @Override
            public void onMealDetailsFetched(Meal meal) {
                List<Meal> mealList = new ArrayList<>();
                mealList.add(meal);
                view.showMealList(mealList); // Pass data to the View
            }

            @Override
            public void onFailure(String message) {
                Log.w("id", "onFailure: " + message);
            }
        });
    }

    @Override
    public void filterByCategory(String selectedCategory) {
        repo.filterByCategory(new NetworkCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.showMealList(meals);
            }

            @Override
            public void onSuccessArea(List<Meal> meals) {

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: filter by cat");
            }
        }, selectedCategory);
    }

    @Override
    public void filterByArea(String area) {


        repo.filterByArea(new NetworkCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.showMealList(meals);
                view.showListOfMealByArea(meals);

            }

            @Override
            public void onSuccessArea(List<Meal> meals) {

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: filter by area");
            }


        }, area);
    }

    @Override
    public void filterByIngredient(String ingredient) {
        repo.filterByIngredient(new NetworkCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.showListOfMealByIngredient(meals);
                view.showMealList(meals);
                Log.i("cat", "onSuccess: ingredient");
            }

            @Override
            public void onSuccessArea(List<Meal> meals) {

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i("cat", "onFailure: ingred");
            }

        }, ingredient);
    }
}
